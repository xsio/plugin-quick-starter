import React, { Component } from 'react';
import { Popover, Col, message, Row, Icon, Button } from 'antd';
import FolderService from '../services/list';
import { GroupMenu } from 'convertlab-uilib';
// import { GOODS_DISPLAY } from "../util";
import './GroupMenu.less';

const DESC_MAP = {
    list: '群组',
    page: '微页面',
    sms: '短消息',
    mail: '邮件',
    flow: '自动流',
    wechatTpl: '微信模版消息',
    referPlan: '推广活动',
    // goodsGroup: `${GOODS_DISPLAY.name}群组`,
    webhook: 'Webhook',
    campaign: '营销活动',
    wechatCard: '微信卡券',
    luckyDraw: '抽奖活动'
};

class MoveButton extends Component {
    componentWillMount() {
        this.state = {
            searchText: '',
            visible: false,
        };
    }

    refreshMenu = (folderId) => {
        FolderService.list(this.props.type).then((items) => {
            if (this.props.refresh) {
                this.props.refresh('move', { folderId, items });
            }
        });
    };

    moveToGroup = (folderId, moveIds, showMessage = true) => {
        if (folderId !== 'search') {
            const { type } = this.props;
            moveIds = moveIds || this.props.moveIds || [];

            // FolderService.moveTo(folderId, moveIds, type).then(() => {
            //     const desc = DESC_MAP[this.props.type] || '数据';
            //     showMessage && message.success(`已成功将${moveIds.length}个${desc}移到指定的分组`);
            //     this.setState({ visible: false, searchText: '' });
            //     this.refreshMenu(folderId);
            // });
        }
    };

    onVisibleChange = (visible) => this.setState({ visible });

    render() {
        const { NAKey = 'NONE', ALLKey = 'ALL', data, moveIds = [], editable = true,
            parentKey = 'parent', dataKey = 'id' } = this.props;
        const { visible } = this.state;

        const disabled = !editable || moveIds.length === 0;

        return (
            <Popover
                content={(
                    <GroupMenu
                        className="group-menu-button"
                        selectedKeys={moveIds}
                        onSelect={(value) => this.moveToGroup(value[0])}
                        ALLKey={ALLKey}
                        NAKey={NAKey}
                        parentKey={parentKey}
                        dataKey={dataKey}
                        data={[{ id: NAKey, name: '未分组', contents: [] }].concat(data)}
                        editable={false}
                        theme="white"
                        searchPlaceholder="搜索分组"
                        size="small"
                        excludeMap={{ [ALLKey]: true }}
                    />
                )}
                visible={visible}
                onVisibleChange={!disabled && this.onVisibleChange}
                placement="rightBottom"
                trigger="click"
            >
                <Button type="primary" disabled={disabled} className="group-menu-button-placeholder">
                    移到分组<Icon type="down" />
                </Button>
            </Popover>
        );
    }
}



export default class FullGroupMenu extends Component {
    componentWillMount() {
        this.refreshMenu();
        this.state = {
            loading: false
        };
    }

    componentWillUnmount() {
        clearTimeout(this.selectTimeout);
    }

    isNotLoading = () => {
        if (this.state.loading) {
            message.info(`正在加载${DESC_MAP[this.props.type] || '数据'}，请稍后...`);
            return false;
        }
        return true;
    };

    refreshMenu = (type = 'get', args) => {
        if (!(args instanceof Object)) {
            args = {};
        }
        this.setState({ loading: true });
        // FolderService.list(this.props.type).then((items) => {
        //     if (this.props.refresh) {
        //         args.items = items;
        //         this.props.refresh(type, args);
        //     }

        //     this.setState({ loading: false });
        // }).fail(() => {
        //     this.setState({ loading: false });
        // });
    };

    createGroup = (item) => {
        if (this.isNotLoading()) {
            this.setState({ loading: true });

            // FolderService.create(item.name, item.parent, this.props.type).then(() => {
            //     message.success('分组创建成功。');
            //     this.refreshMenu('create');
            //     this.setState({ loading: false });
            // }).fail(() => {
            //     message.success('分组创建失败。');
            //     this.setState({ loading: false });
            // });
        }
        return true;
    };

    updateGroup = (data, name) => {
        if (this.isNotLoading()) {
            this.setState({ loading: true });
            // FolderService.update({ id: data.id, name, type: this.props.type }).then(() => {
            //     message.info('分组修改成功');
            //     this.refreshMenu('update');
            //     this.setState({ loading: false });
            // }).fail(() => {
            //     message.success('分组修改失败。');
            //     this.setState({ loading: false });
            // });
        }
        return true;
    };

    deleteGroup = (item) => {
        if (item.contents.length > 0) {
            message.error('分组不为空');
        } else if (this.isNotLoading()) {
            this.setState({ loading: true });
            // FolderService.remove(item.id).then((result) => {
            //     if (result.error) {
            //         message.error(result.error.message);
            //     } else {
            //         message.info('删除分组成功');
            //         this.refreshMenu('delete');
            //     }
            //     this.setState({ loading: false });
            // }).fail(() => {
            //     message.info('删除分组失败');
            //     this.setState({ loading: false });
            // });
        }
        return true;
    };

    _onSelect = (keys, item) => {
        if (this.props.refresh) {
            this.props.refresh('click', { folderId: keys[0], item });
        }
    };

    onSelect = (keys, item) => {
        clearTimeout(this.selectTimeout);
        this.selectTimeout = setTimeout(() => {
            this._onSelect(keys, item)
        }, 200);
    };

    onDrop = (data) => {
        if (!data.dropToGap) {
            // FolderService.update({
            //     id: data.dragKey,
            //     parent: data.dropKey === 'ALL' ? 0 : data.dropKey,
            //     type: this.props.type
            // }).then(() => {
            //     this.refreshMenu();
            // });
        }
        return true;
    };

    render() {
        const {
            style, className, ALLKey, NAKey, draggable, dragInGap, type,
            editable = true, deletable, dataKey, parentKey, data, selectedKey, maxLevel = 3, loading,
            searchPlaceholder
        } = this.props;

        const enableDrop = draggable === undefined ? maxLevel > 1 : draggable;


        return (
            <GroupMenu
                style={style}
                className={className + `${dragInGap ? '' : ' no-drag-in-gap'}`}
                maxLevel={maxLevel}
                selectedKeys={[selectedKey]}
                NAKey={NAKey || 'NONE'}
                ALLKey={ALLKey || 'ALL'}
                parentKey={parentKey || 'parent'}
                editable={editable}
                deletable={deletable}
                dataKey={dataKey || 'id'}
                data={data}
                draggable={enableDrop}
                onSelect={this.onSelect}
                onUpdateName={this.updateGroup}
                onAddChild={this.createGroup}
                onDelete={this.deleteGroup}
                onDrop={enableDrop ? this.onDrop : undefined}
                loading={loading || this.state.loading}
                contentsKey="contents"
                // itemLabel={DESC_MAP[type]}
                searchPlaceholder={searchPlaceholder}
            />
        );
    }
}

FullGroupMenu.MoveButton = MoveButton;
