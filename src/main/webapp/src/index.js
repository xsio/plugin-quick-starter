import React from 'react';
import ReactDOM from 'react-dom';
import TemplateList from './TemplateList';
import { LocaleProvider } from 'antd';
import enUS from 'antd/es/locale-provider/en_US';
import zhCN from 'antd/es/locale-provider/zh_CN';
import './index.less'

import { Router, hashHistory, IndexRedirect, Route } from 'react-router';

ReactDOM.render((
  <LocaleProvider locale={localStorage.locale ? enUS : zhCN}>
    <Router history={hashHistory}>
      <Route path="/">
        <IndexRedirect to="/templates" />
        <Route path="/templates" component={TemplateList} />
      </Route>
    </Router>
	</LocaleProvider>
), document.getElementById('content'));
