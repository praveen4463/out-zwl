// See https://github.com/testing-library/user-event/issues/440#issue-689935956
// minify using `uglifyjs dragAndDrop.js -c -m -o dragAndDrop.min.js`
const fireMouseEvent = function (
    type,
    elem,
    centerX,
    centerY
) {
    const evt = document.createEvent('MouseEvents');
    evt.initMouseEvent(
        type,
        true,
        true,
        window,
        1,
        1,
        1,
        centerX,
        centerY,
        false,
        false,
        false,
        false,
        0,
        elem
    );
    return elem.dispatchEvent(evt);
};

const dragAndDrop = (elemDrag, elemDrop) => {
  let pos = elemDrag.getBoundingClientRect();
  const center1X = Math.floor((pos.left + pos.right) / 2);
  const center1Y = Math.floor((pos.top + pos.bottom) / 2);

  pos = elemDrop.getBoundingClientRect();
  const center2X = Math.floor((pos.left + pos.right) / 2);
  const center2Y = Math.floor((pos.top + pos.bottom) / 2);

  // mouse over dragged element and mousedown
  fireMouseEvent('mousemove', elemDrag, center1X, center1Y);
  fireMouseEvent('mouseenter', elemDrag, center1X, center1Y);
  fireMouseEvent('mouseover', elemDrag, center1X, center1Y);
  fireMouseEvent('mousedown', elemDrag, center1X, center1Y);

  // start dragging process over to drop target
  const dragStarted = fireMouseEvent(
      'dragstart',
      elemDrag,
      center1X,
      center1Y
  );
  if (!dragStarted) {
      return;
  }

  fireMouseEvent('drag', elemDrag, center1X, center1Y);
  fireMouseEvent('mousemove', elemDrag, center1X, center1Y);
  fireMouseEvent('drag', elemDrag, center2X, center2Y);
  fireMouseEvent('mousemove', elemDrop, center2X, center2Y);

  // trigger dragging process on top of drop target
  fireMouseEvent('mouseenter', elemDrop, center2X, center2Y);
  fireMouseEvent('dragenter', elemDrop, center2X, center2Y);
  fireMouseEvent('mouseover', elemDrop, center2X, center2Y);
  fireMouseEvent('dragover', elemDrop, center2X, center2Y);

  // release dragged element on top of drop target
  fireMouseEvent('drop', elemDrop, center2X, center2Y);
  fireMouseEvent('dragend', elemDrag, center2X, center2Y);
  fireMouseEvent('mouseup', elemDrag, center2X, center2Y);
};