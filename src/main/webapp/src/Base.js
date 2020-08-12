import React from 'react';

export default class Base extends React.Component {
  constructor() {
    super();
  }

  componentDidMount() {
    window.addEventListener('message', (e) => {
      const data = e.data;
      if(data && data.type) {
        if (data.type === 'onInitData') {
          this.onInit(data.content.actionBody || {});
        } else if (data.type === 'onConfirm') {
          const data = this.onConfirm();
          this.__sendMessage(data);
        }
      }
    },false);
    this.__onInit();
  }

  __sendMessage = (content) => {
    window.parent.postMessage({
      type: 'cpFinish',
      content: content
    }, '*');
  };

  __onInit = (content) => {
    window.parent.postMessage({
      type: 'cpInit',
      content: content
    }, '*');
  };
}
