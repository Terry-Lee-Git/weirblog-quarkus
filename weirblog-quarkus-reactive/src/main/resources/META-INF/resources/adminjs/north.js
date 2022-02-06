function changeTheme(theme) {
        var easyuiTheme = $('#easyuiTheme');
        var url = easyuiTheme.attr('href');
        var href = url.substring(0, url.indexOf('themes')) + 'themes/' + theme + '/easyui.css';
        easyuiTheme.attr('href', href);

        var $iframe = $('iframe');
        if ($iframe.length > 0) {
            for (var i = 0; i < $iframe.length; i++) {
                var ifr = $iframe[i];
                try {
                    $(ifr).contents().find('#easyuiTheme').attr('href', href);
                } catch (e) {
                    try {
                        ifr.contentWindow.document.getElementById('easyuiTheme').href = href;
                    } catch (e) {
                    }
                }
            }
        }

        $.cookie('easyuiTheme', theme, {
            expires: 7
        });
    }

    function logoutFun() {
        $.post('user/logout', function (j) {
            location.replace('index.jsp');
        }, 'json');
    }

    function lockWindowFun() {
        $.post('user/logout', function (j) {
            $('#loginDialog').dialog('open');
        }, 'json');
    }

    $(function () {
        $('#loginDialog').show().dialog({
            modal: true,
            closable: false,
            iconCls: 'ext-icon-lock_open',
            buttons: [{
                id: 'loginBtn',
                text: '登录',
                handler: function () {
                    loginFun();
                }
            }],
            onOpen: function () {
                $('#weirpwd').val('');
                $('form :input').keyup(function (event) {
                    if (event.keyCode == 13) {
                        loginFun();
                    }
                });
            }
        }).dialog('close');

    });

    function loginFun() {
        if ($("#loginForm").form('validate')) {
            $('#loginBtn').linkbutton('disable');
            $.post('user/login', $("#loginForm").serialize(), function (j) {
                if (j.success) {
                    $('#loginDialog').dialog('close');
                } else {
                    $.messager.alert('提示', j.msg, 'error', function () {
                        $('#weirname').focus();
                    });
                }
                $('#loginBtn').linkbutton('enable');
            }, 'json');
        }
    }