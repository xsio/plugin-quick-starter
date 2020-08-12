import React, { Component } from 'react';
import { CollapsibleMenu } from 'convertlab-uilib';
import { MENUS } from '../constants/index';

let collapseState;
export default class LeftMenu extends Component {
    componentWillMount() {
        const { children } = this.props;

        if (collapseState === undefined) {
            collapseState = !!children;
        }
    }

    onSelectKey = (key) => {
        //TODO, if tab is the same need refresh
        // if (this.props.router.location.pathname.indexOf(key) == -1) {
        //     this.props.router.push(key);
        // }
    };

    onCollapsed = (collapsed) => {
        collapseState = collapsed;
    };

    render() {
        const { selectedKey, children } = this.props;

        return (
            <CollapsibleMenu
                onCollapsed={this.onCollapsed}
                expandContent={children}
                menus={MENUS}
                collapsed={collapseState}
                selectedKey={selectedKey}
                onSelectKey={this.onSelectKey}
            />
        )
    }
}
