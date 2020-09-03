// See https://github.com/testing-library/dom-testing-library
// Copyright (c) 2017 Kent C. Dodds
// This is a modified version of the piece of code taken from dom-testing-library
const TEXT_NODE = 3;
const IGNORED_NODES = 'html, head, style, script, link, meta, title';
const SELECTOR = '*';

function getNodeText(node) {
  // element.matches https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
  if (node.matches('input[type=submit], input[type=button]')) {
    return node.value;
  }
  return Array.from(node.childNodes)
    .filter(
      (child) => child.nodeType === TEXT_NODE && Boolean(child.textContent)
    )
    .map((c) => c.textContent)
    .join('');
}

function checkContainerType(container) {
  function getTypeName(object) {
    if (typeof object === 'object') {
      return object === null ? 'null' : object.constructor.name;
    }
    return typeof object;
  }

  if (
    !container ||
    !(typeof container.querySelector === 'function') ||
    !(typeof container.querySelectorAll === 'function')
  ) {
    throw new TypeError(
      `Expected 'fromElement' to be an Element, a Document or a DocumentFragment but got ${getTypeName(
        container
      )}.`
    );
  }
}

function matches(textToMatch, matcher) {
  if (typeof textToMatch !== 'string') {
    return false;
  }
  const normalizedText = textToMatch.trim();
  if (typeof matcher === 'string') {
    return normalizedText === matcher;
  }
  return matcher.test(normalizedText);
}

function findAllByText(container, text) {
  checkContainerType(container);
  let matcher = text;
  // Following regex matches whether the string is a valid js regex, it's not accurate as
  // repeated flags are accepted but it almost work as expected. There are two groups, the
  // first matches actual regex inside /.../ and another matches flags, we will create RegExp
  // using these two.
  const regexMatches = matcher.match(/^\/(.+?)\/([gimsuy]{0,6})$/);
  if (Array.isArray(regexMatches)) {
    const [, regex, flags] = regexMatches;
    // when regex is invalid, RegExp throws error which is what we want.
    matcher = new RegExp(regex, flags);
  }
  let baseArray = [];
  if (typeof container.matches === 'function' && container.matches(SELECTOR)) {
    baseArray = [container];
  }
  return [...baseArray, ...Array.from(container.querySelectorAll(SELECTOR))]
    .filter((node) => !node.matches(IGNORED_NODES))
    .filter((node) => matches(getNodeText(node), matcher));
}

const [container, text] = arguments;
return findAllByText(container ?? document.querySelector('*'), text);
