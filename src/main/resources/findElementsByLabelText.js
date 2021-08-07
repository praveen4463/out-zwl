// See https://github.com/testing-library/dom-testing-library
// Copyright (c) 2017 Kent C. Dodds
// minify using `uglifyjs findElementsByLabelText.js -c -m reserved=['findAllByLabelText'] -o findElementsByLabelText.min.js`
const TEXT_NODE = 3;
const SELECTOR = '*';
const labelledNodeNames = [
  'button',
  'meter',
  'output',
  'progress',
  'select',
  'textarea',
  'input',
];

function checkContainerType(container) {
  if (
    !container ||
    !(typeof container.querySelector === 'function') ||
    !(typeof container.querySelectorAll === 'function')
  ) {
    throw new TypeError(
      `Expected container to be an Element, a Document or a DocumentFragment but got ${getTypeName(
        container
      )}.`
    );
  }

  function getTypeName(object) {
    if (typeof object === 'object') {
      return object === null ? 'null' : object.constructor.name;
    }
    return typeof object;
  }
}

function getTextContent(node) {
  if (labelledNodeNames.includes(node.nodeName.toLowerCase())) {
    return '';
  }

  if (node.nodeType === TEXT_NODE) {
    return node.textContent;
  }

  return Array.from(node.childNodes)
    .map((childNode) => getTextContent(childNode))
    .join('');
}

function getLabelContent(element) {
  let textContent;
  if (element.tagName.toLowerCase() === 'label') {
    textContent = getTextContent(element);
  } else {
    textContent = element.value || element.textContent;
  }
  return textContent;
}

// Based on https://github.com/eps1lon/dom-accessibility-api/pull/352
function getRealLabels(element) {
  if (element.labels !== undefined) {
    return element.labels ?? [];
  }

  if (!isLabelable(element)) {
    return [];
  }

  const labels = element.ownerDocument.querySelectorAll('label');
  return Array.from(labels).filter((label) => label.control === element);
}

function isLabelable(element) {
  return (
    /BUTTON|METER|OUTPUT|PROGRESS|SELECT|TEXTAREA/.test(element.tagName) ||
    (element.tagName === 'INPUT' && element.getAttribute('type') !== 'hidden')
  );
}

function getLabels(container, element) {
  const ariaLabelledBy = element.getAttribute('aria-labelledby');
  const labelsId = ariaLabelledBy ? ariaLabelledBy.split(' ') : [];
  return labelsId.length
    ? labelsId.map((labelId) => {
        const labellingElement = container.querySelector(`[id="${labelId}"]`);
        return labellingElement
          ? {content: getLabelContent(labellingElement), formControl: null}
          : {content: '', formControl: null};
      })
    : Array.from(getRealLabels(element)).map((label) => {
        const textToMatch = getLabelContent(label);
        const formControlSelector =
          'button, input, meter, output, progress, select, textarea';
        const labelledFormControl = Array.from(
          label.querySelectorAll(formControlSelector)
        ).filter((formControlElement) =>
          formControlElement.matches(SELECTOR)
        )[0];
        return {content: textToMatch, formControl: labelledFormControl};
      });
}

function matches(textToMatch, matchWith) {
  if (typeof textToMatch !== 'string') {
    return false;
  }
  const normalizedText = textToMatch.trim();
  if (typeof matchWith === 'string') {
    return normalizedText === matchWith;
  }
  return matchWith.test(normalizedText);
}

function getMatchWith(givenText) {
  let matchWith = givenText;
  // Following regex matches whether the string is a valid js regex, it's not accurate as
  // repeated flags are accepted but it almost work as expected. There are two groups, the
  // first matches actual regex inside /.../ and another matches flags, we will create RegExp
  // using these two.
  const regexMatches = matchWith.match(/^\/(.+?)\/([gimsuy]{0,6})$/);
  if (Array.isArray(regexMatches)) {
    const regex = regexMatches[1];
    const flags = regexMatches[2];
    // when regex is invalid, RegExp throws error which is what we want.
    matchWith = new RegExp(regex, flags);
  }
  return matchWith;
}

const findAllByLabelText = (container, text) => {
  checkContainerType(container);

  const matcher = matches;
  const matchWith = getMatchWith(text);

  const matchingLabelledElements = Array.from(container.querySelectorAll('*'))
    .filter((element) => {
      return (
        getRealLabels(element).length || element.hasAttribute('aria-labelledby')
      );
    })
    .reduce((labelledElements, labelledElement) => {
      const labelList = getLabels(container, labelledElement);
      labelList
        .filter((label) => Boolean(label.formControl))
        .forEach((label) => {
          if (matcher(label.content, matchWith) && label.formControl) {
            labelledElements.push(label.formControl);
          }
        });
      const labelsValue = labelList
        .filter((label) => Boolean(label.content))
        .map((label) => label.content);
      if (matcher(labelsValue.join(' '), matchWith)) {
        labelledElements.push(labelledElement);
      }
      if (labelsValue.length > 1) {
        labelsValue.forEach((labelValue, index) => {
          if (matcher(labelValue, matchWith)) {
            labelledElements.push(labelledElement);
          }

          const labelsFiltered = [...labelsValue];
          labelsFiltered.splice(index, 1);

          if (labelsFiltered.length > 1) {
            if (matcher(labelsFiltered.join(' '), matchWith)) {
              labelledElements.push(labelledElement);
            }
          }
        });
      }

      return labelledElements;
    }, []);

  return Array.from(new Set(matchingLabelledElements)).filter((element) =>
    element.matches(SELECTOR)
  );
};
