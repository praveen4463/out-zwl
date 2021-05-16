// See https://github.com/testing-library/dom-testing-library
// Copyright (c) 2017 Kent C. Dodds
// This is a modified version of the piece of code taken from dom-testing-library, supports IE11
// minify using `uglifyjs findElementsByText.js -c -m reserved=['findAllByText'] -o findElementsByText.min.js`
const TEXT_NODE = 3;
const IGNORED_NODES = 'html, head, style, script, link, meta, title';
const SELECTOR = '*';

// polyfill to support Element.matches
if (!Element.prototype.matches) {
  Element.prototype.matches =
      Element.prototype.matchesSelector ||
      Element.prototype.mozMatchesSelector ||
      Element.prototype.msMatchesSelector ||
      Element.prototype.oMatchesSelector ||
      Element.prototype.webkitMatchesSelector ||
      function(s) {
        var matches = (this.document || this.ownerDocument).querySelectorAll(s),
            i = matches.length;
        while (--i >= 0 && matches.item(i) !== this) {}
        return i > -1;
      };
}

function getNodeText(node) {
  // element.matches https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
  if (node.matches('input[type=submit], input[type=button]')) {
    return node.value;
  }
  const nodeChildren = node.childNodes;
  let clone = [];
  for (let i = 0; i < nodeChildren.length; i++) {
    clone.push(nodeChildren[i]);
  }
  return clone
    .filter(function(child) {
      return child.nodeType === TEXT_NODE && Boolean(child.textContent);
    })
    .map(function(child) {
      return child.textContent;
    })
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
    const containerType = getTypeName(container);
    throw new TypeError('Expected fromElement to be an Element, a Document or a DocumentFragment but got ' + containerType);
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
    const regex = regexMatches[1];
    const flags = regexMatches[2];
    // when regex is invalid, RegExp throws error which is what we want.
    matcher = new RegExp(regex, flags);
  }
  let baseArray = [];
  if (typeof container.matches === 'function' && container.matches(SELECTOR)) {
    baseArray = [container];
  }
  const containerChildren = container.querySelectorAll(SELECTOR);
  let clone = baseArray.slice(0);
  for (let i = 0; i < containerChildren.length; i++) {
    clone.push(containerChildren[i]);
  }
  return clone
    .filter(function(node) {
      return !node.matches(IGNORED_NODES);
    })
    .filter(function(node) {
      return matches(getNodeText(node), matcher);
    });
}
