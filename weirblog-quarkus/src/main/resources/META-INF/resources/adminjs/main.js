var west_tree, mainTabs;
        $(function () {
            west_tree = $('#west_tree').tree({
                lines: true,
                method: 'get',
                url: '/menu/tree',
                onLoadSuccess: function (node, data) {
                    if (data) {
                        $(data).each(function (index, d) {
                            if (this.state == 'closed') {
                                west_tree.tree('expandAll');
                            }
                        });
                    }
                },
                onClick: function (node) {
                    if (node.attributes && node.attributes.url) {
                        var url = '' + node.attributes.url;
                        var tabs = $('#mainTabs');
                        var opts = {
                            title: node.text,
                            closable: true,
                            content: '<iframe src="' + url + '" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>',
                            border: false,
                            fit: true
                        };
                        if (tabs.tabs('exists', opts.title)) {
                            tabs.tabs('select', opts.title);
                        } else {
                            tabs.tabs('add', opts);
                        }
                    }
                },
                onDblClick: function (node) {
                    if (node.state == 'closed') {
                        $(this).tree('expand', node.target);
                    } else {
                        $(this).tree('collapse', node.target);
                    }
                }
            });

            mainTabs = $('#mainTabs').tabs({
                fit: true,
                border: false,
                tools: [{
                    iconCls: 'ext-icon-arrow_up',
                    handler: function () {
                        mainTabs.tabs({
                            tabPosition: 'top'
                        });
                    }
                }, {
                    iconCls: 'ext-icon-arrow_left',
                    handler: function () {
                        mainTabs.tabs({
                            tabPosition: 'left'
                        });
                    }
                }, {
                    iconCls: 'ext-icon-arrow_down',
                    handler: function () {
                        mainTabs.tabs({
                            tabPosition: 'bottom'
                        });
                    }
                }, {
                    iconCls: 'ext-icon-arrow_right',
                    handler: function () {
                        mainTabs.tabs({
                            tabPosition: 'right'
                        });
                    }
                }, {
                    text: '刷新',
                    iconCls: 'ext-icon-arrow_refresh',
                    handler: function () {
                        var panel = mainTabs.tabs('getSelected').panel('panel');
                        var frame = panel.find('iframe');
                        try {
                            if (frame.length > 0) {
                                for (var i = 0; i < frame.length; i++) {
                                    frame[i].contentWindow.document.write('');
                                    frame[i].contentWindow.close();
                                    frame[i].src = frame[i].src;
                                }
                                if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
                                    try {
                                        CollectGarbage();
                                    } catch (e) {
                                    }
                                }
                            }
                        } catch (e) {
                        }
                    }
                }, {
                    text: '关闭',
                    iconCls: 'ext-icon-cross',
                    handler: function () {
                        var index = mainTabs.tabs('getTabIndex', mainTabs.tabs('getSelected'));
                        var tab = mainTabs.tabs('getTab', index);
                        if (tab.panel('options').closable) {
                            mainTabs.tabs('close', index);
                        } else {
                            $.messager.alert('提示', '[' + tab.panel('options').title + ']不可以被关闭！', 'error');
                        }
                    }
                }]
            });
        });