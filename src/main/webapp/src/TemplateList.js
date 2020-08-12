import React from "react";
import { TitleBar } from "convertlab-ui-common";
import LeftMenu from "./components/LeftMenu";
import { MainMenu } from "convertlab-ui-common";
import FullGroupMenu from "./components/GroupMenu";
import { Tooltip, Row, Icon, Table, Col, Spin, message,Badge,Modal,Button } from "antd";
import { SearchInput, IconButton } from "convertlab-uilib";


const DIALOG_TYPE = {
  PREVIEW:"preview"
};

class TemplateList extends React.Component {
  componentWillMount() {
    this.state = {
      lists: [],
      q: "",
      page: 1,
      folders: [],
      selectedFolder: { id: "ALL" },
      loading: true,
      selectedRowKeys: [],
      expandedRowKeys: [],
      searchText: "",
      groupMap: {},
      dialog: {},
      quota: 0
    };
    
  }

  componentDidMount(){
    this.loadTemplates();
  }


  loadTemplates = (props = this.props, query = this.state.q, page = this.state.page, folder = this.state.selectedFolder) => {
    this.setState({ loading: true });
    let groupId = folder.id;
    if (folder.id == "ALL") {
      groupId = -1;
    }
    if (folder.id == "NONE") {
      groupId = 0;
    }

    //在这里加载数据
    // MmsService.templateList(query, page, groupId).then(data => {
    //   this.setState({ lists });
    // });

    let lists = [{ "id":7,"dateCreated":"2020-04-16T07:54:20Z","groupId":"0","lastUpdated":"2020-04-16T07:54:25Z","name":"Item 1","createName":"demo","type":"marketing","createId":"41","status":"pass" }]
    this.setState({ lists, loading: false, total: 1, loadingSendCount: false });
  };


  getRightButtons = () => {
    const { router } = this.props;
    const right_buttons = [];
    right_buttons.push({
      txt: "新建",
      type: "plus",
      fn: () => {}
      // fn: () => this.createTemplate()
    });

    return right_buttons;
  };

  getColumns = () => {
    const columns = [
      { title: "名称", dataIndex: "name", className: "width10em" },
      {
        title: "类型",
        className: "width10em",
        dataIndex: "type",
        render: (value) => {
          return value == "notification" ? "通知类" : "营销类";
        }
      },
      {
        title: "状态",
        className: "width10em",
        dataIndex: "status",
        render: (value) => {
          let reason = {
            status:"default",
            text:"草稿"
          }
          if(value == "pass"){
            reason.status = 'success';
            reason.text = '通过';
          }
          return <Badge {...reason} />;
        }
      },
      {
        title: "预览",
        className: "width10em",
        render: (value,record) => {
          return (
            <span onClick={() => this.openPreview(record)}  style={{ color:"#39c6cd",cursor:"pointer" }}> 预览</span>
          )
        }
      },
      {
        title: "创建时间",
        dataIndex: "dateCreated",
        className: "width5em",
        align: "center",
        render: (date, record) => {
          const result = (
            <div style={{ whiteSpace: "nowrap" }}>
              {date ? moment(date).format("YYYY-MM-DDTHH:mm:ss") : ""}
            </div>
          );
          return result;
        }
      },
      {
        title: "创建人",
        dataIndex: "createName",
        className: "width5em",
        align: "center",
        render: (date, record) => {
          const result = (
            <div style={{ whiteSpace: "nowrap" }}>
              {record.createName}
            </div>
          );
          return result;
        }
      }
    ];
    return columns.concat({
      title: "操作",
      dataIndex: "action",
      className: "width5em",
      render: (value, record) => {
        const buttons = [];

        const deleteButton = {
          text: "删除",
          danger: true,
          icon: "close",
          // disabled: loading || !this.deletable,
          popUp: {
            title: `确定要删除吗?`,
            onConfirm: () => {
              
            }
          }
        };
        const { router } = this.props;

        const editButton = {
          icon: "edit",
          text: "编辑",
          disabled: record.status == 'pass',
          onClick: () => {}
        };
        //
        const copyButton = {
          text: "复制",
          icon: "copy",
          // disabled: loading,
          onClick: () => {
            
          }
        };

        buttons.push(editButton);
        buttons.push(copyButton);
        buttons.push(deleteButton);

        return (
          <div style={{ whiteSpace: "nowrap" }}>
            {buttons.map((btn, index) => <IconButton {...btn} key={index}/>)}
          </div>
        );
      }
    });
  };

  renderTitle = () => {
    const { quota } = this.state;
    return (
      <Row type="flex" align="middle" justify="space-between" className="mms-template-list-title cl-panel">
        <Row type="flex" align="middle" gutter={16}>
          <Col>插件项目快速开始模板</Col>
          
          <span style={{ padding: "0 4px" }}>|</span>
          <Tooltip
            trigger='click'
            title={
              <div>
                请参考开发文档
              </div>
            }
            placement="bottom"
            mouseLeaveDelay={0.5}
          >
            <a href="javacript:;" className="text-dec-none">帮助</a>
          </Tooltip>
        </Row>
      </Row>
    );
  };

  expandedRowRender = (record) => {
    const sendRecords = this.state.massSendList[record.id];

    return (
      <MassSendCmp
        sendRecords={sendRecords}
      />
    );
  };


  openPreview = record => {
    this.setState({
      dialogVisible: true,
      dialogType: DIALOG_TYPE.PREVIEW,
      dialogTypeProps: { record }
    });
  }

  onClose = () => {
    this.setState({
      dialogVisible: false,
      dialogType: null,
      dialogTypeProps: {}
    });
  };

  renderDialog = () => {
    const { dialogType, dialogTypeProps,quota } = this.state;
    switch (dialogType) {
      case DIALOG_TYPE.PREVIEW:
        const { record } = dialogTypeProps;
        return (
          <Modal
            visible={true}
            title={
              <span style={{ display:"flex",alignItems:"center",color:"#a2b4be",fontSize:"14px" }}>
              <Icon style={{ marginRight:"10px" }} type="setting" />
                预览
            </span>
            }
            width={350}
            closable={false}
            footer={
              <Button
                style={{ backgroundColor:"#39c6cd",
                  borderColor: "#39c6cd",
                color:"#fff" }}
                onClick={this.onClose}>确定</Button>}
            className="mms-preview"
          >
            <div style={{ maxHeight:"500px",
              // height:"500px",
              overflowY:"scroll" }}>
              <li style={{ color:"#444",marginBottom:"5px" }}>内容预览</li>
              
            </div>
          </Modal>
        )
    }
  };

  onChangeSelectedRowKeys = (selectedRowKeys) => this.setState({ selectedRowKeys });

  search = q => {
    this.setState({ q }, () => {
      this.loadTemplates();
    });
  };

  render() {
    const { router } = this.props;
    const { folders, selectedFolder, loading, lists, expandedRowKeys, selectedRowKeys, page, total } = this.state;

    return (
      <div className="cl-layout">
        <MainMenu/>
        <TitleBar title={{ txt: "项目模板" }} right_buttons={this.getRightButtons()}/>
        <div className="cl-layout-content">
          <LeftMenu router={router} selectedKey="templates">
            <FullGroupMenu
              selectedKey={selectedFolder.id}
              data={folders}
              loading={loading}
              type="mms"
              refresh={this.refresh}
            />
          </LeftMenu>
          <div className="cl-layout-page">
            {this.renderTitle()}
            <div className="cl-panel">
              <Row className="cl-panel-head" type="flex" justify="space-between">
                <Col>
                  <Row type="flex" gutter={16}>
                    <Col>
                      <SearchInput
                        placeholder="搜索内容"
                        onSearch={(q) => this.search(q)}
                      />
                    </Col>
                  </Row>
                </Col>
              </Row>
              <Table
                expandIcon={false}
                expandIconAsCell={false}
                expandRowByClick={true}
                expandedRowRender={this.expandedRowRender}
                expandedRowKeys={expandedRowKeys}
                // loading={loading}
                rowSelection={{
                  selectedRowKeys: selectedRowKeys,
                  onChange: this.onChangeSelectedRowKeys
                }}
                rowKey="id"
                columns={this.getColumns()}
                dataSource={lists}
                pagination={{
                  current: page,
                  total: total,
                  size: 10,
                  onChange: (page) => {this.setState({ page },() => {this.loadTemplates()})},
                  showTotal: (total, range) => `共 ${total} 个, 第 ${range[0]}-${range[1]} 个`,
                }}
              />
            </div>
            {/*<div className="mms-template-list-table">*/}
            {/*</div>*/}
          </div>
        </div>
        {this.renderDialog()}
      </div>
    );
  }
}

export default TemplateList;
