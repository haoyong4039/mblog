var MdEditor = {
    editorId: null,
    format: {
        undo: function undo(editor) {
            editor.undo();
        },

        redo: function redo(editor) {
            editor.redo();
        },

        setBold: function setBold(editor) {
            editor.replaceSelection(`**${editor.getSelection()}**`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 2
            });
            editor.focus();
        },

        setItalic: function setItalic(editor) {
            editor.replaceSelection(`*${editor.getSelection()}*`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 1
            });
            editor.focus();
        },

        setBlockQuote: function setBlockQuote(editor) {
            var cursorLine = editor.getCursor().line;
            editor.replaceRange('> ', {
                line: cursorLine,
                ch: 0
            }, {
                line: cursorLine,
                ch: 0
            });
            editor.focus();
        },

        setHeader: function setHeader(editor, level) {
            var cursorLine = editor.getCursor().line;
            switch (level) {
                case 1:
                    editor.replaceRange('# ', {
                        line: cursorLine,
                        ch: 0
                    }, {
                        line: cursorLine,
                        ch: 0
                    });
                    break;
                case 2:
                    editor.replaceRange('## ', {
                        line: cursorLine,
                        ch: 0
                    }, {
                        line: cursorLine,
                        ch: 0
                    });
                    break;
                case 3:
                    editor.replaceRange('### ', {
                        line: cursorLine,
                        ch: 0
                    }, {
                        line: cursorLine,
                        ch: 0
                    });
                    break;
                case 4:
                    editor.replaceRange('#### ', {
                        line: cursorLine,
                        ch: 0
                    }, {
                        line: cursorLine,
                        ch: 0
                    });
                    break;
                case 5:
                    editor.replaceRange('##### ', {
                        line: cursorLine,
                        ch: 0
                    }, {
                        line: cursorLine,
                        ch: 0
                    });
                    break;
                default:
                    break;
            }
            editor.focus();
        },

        setLink: function setLink(editor) {
            editor.replaceSelection(`[](https://)`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 11
            });
            editor.focus();
        },

        setImage: function setImage(editor) {
            editor.replaceSelection(`![]()`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 3
            });
            editor.focus();
        },

        setFont: function setFont(editor) {
            editor.replaceSelection(`<font size="3">${editor.getSelection()}</font>`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 7
            });
            editor.focus();
        },

        setCenter: function setCenter(editor) {
            editor.replaceSelection(`<center>${editor.getSelection()}</center>`);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 9
            });
            editor.focus();
        },

        setTable: function setTable(editor) {
            var str = '<table>\n' +
                '\t<tr>\n' +
                '\t    <th colspan="2">参数列表</th>\n' +
                '\t</tr>\n' +
                '\t<tr>\n' +
                '\t    <td>参数名</td>\n' +
                '\t    <td>参数值</td>\n' +
                '\t</tr>\n' +
                '</table>';

            editor.replaceSelection(str);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch
            });
            editor.focus();
        },

        uploadPdf: function uploadVideo(editor) {
            var input_f = $('<input type="file" name="file" accept="application/pdf">');
            input_f.on('change', function () {
                var file = input_f[0].files[0];
                if (!file) {
                    return false;
                }
                var form = new FormData();
                form.append("file", file);

                layer.msg('pdf上传中...', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                })

                $.ajax({
                    url: _MTONS.BASE_PATH + "/qny/upload",
                    data: form,
                    type: "POST",
                    cache: false, //上传文件无需缓存
                    processData: false, //用于对data参数进行序列化处理 这里必须false
                    contentType: false, //必须
                    success: function (result) {
                        layer.closeAll();
                        if (result.status === 200) {
                            var sourceUrl = "http://wlinkimg.wulian.cc/" + result.path;
                            var pdf = '['+result.name+']('+sourceUrl+')';

                            editor.replaceSelection(pdf);
                            var cursor = editor.getCursor();
                            editor.setCursor({
                                line: cursor.line,
                                ch: cursor.ch
                            });
                            editor.focus();
                        } else {
                            layer.alert(result.message);
                        }
                    },
                    error: function () {
                        layer.closeAll();
                    }
                });
            });

            input_f.click();
        },

        uploadZip: function uploadZip(editor) {
            var input_f = $('<input type="file" name="file" accept="application/zip">');
            input_f.on('change', function () {
                var file = input_f[0].files[0];
                if (!file) {
                    return false;
                }
                var form = new FormData();
                form.append("file", file);

                layer.msg('zip上传中...', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                })

                $.ajax({
                    url: _MTONS.BASE_PATH + "/qny/upload",
                    data: form,
                    type: "POST",
                    cache: false, //上传文件无需缓存
                    processData: false, //用于对data参数进行序列化处理 这里必须false
                    contentType: false, //必须
                    success: function (result) {
                        layer.closeAll();
                        if (result.status === 200) {
                            var sourceUrl = "http://wlinkimg.wulian.cc/" + result.path;
                            var zip = '['+result.name+']('+sourceUrl+')';

                            editor.replaceSelection(zip);
                            var cursor = editor.getCursor();
                            editor.setCursor({
                                line: cursor.line,
                                ch: cursor.ch
                            });
                            editor.focus();
                        } else {
                            layer.alert(result.message);
                        }
                    },
                    error: function () {
                        layer.closeAll();
                    }
                });
            });

            input_f.click();
        },

        uploadExcel: function uploadExcel(editor) {
            var input_f = $('<input type="file" name="file" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">');
            input_f.on('change', function () {
                var file = input_f[0].files[0];
                if (!file) {
                    return false;
                }
                var form = new FormData();
                form.append("file", file);

                layer.msg('excel上传中...', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                })

                $.ajax({
                    url: _MTONS.BASE_PATH + "/qny/upload",
                    data: form,
                    type: "POST",
                    cache: false, //上传文件无需缓存
                    processData: false, //用于对data参数进行序列化处理 这里必须false
                    contentType: false, //必须
                    success: function (result) {
                        layer.closeAll();
                        if (result.status === 200) {
                            var sourceUrl = "http://wlinkimg.wulian.cc/" + result.path;
                            var zip = '['+result.name+']('+sourceUrl+')';

                            editor.replaceSelection(zip);
                            var cursor = editor.getCursor();
                            editor.setCursor({
                                line: cursor.line,
                                ch: cursor.ch
                            });
                            editor.focus();
                        } else {
                            layer.alert(result.message);
                        }
                    },
                    error: function () {
                        layer.closeAll();
                    }
                });
            });

            input_f.click();
        },

        uploadVideo: function uploadVideo(editor) {
            var input_f = $('<input type="file" name="file" accept="video/mp4">');
            input_f.on('change', function () {
                var file = input_f[0].files[0];
                if (!file) {
                    return false;
                }
                var form = new FormData();
                form.append("file", file);

                layer.msg('视频上传中...', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                })

                $.ajax({
                    url: _MTONS.BASE_PATH + "/qny/upload",
                    data: form,
                    type: "POST",
                    cache: false, //上传文件无需缓存
                    processData: false, //用于对data参数进行序列化处理 这里必须false
                    contentType: false, //必须
                    success: function (result) {
                        layer.closeAll();
                        if (result.status === 200) {
                            var sourceUrl = "http://wlinkimg.wulian.cc/" + result.path;
                            var video = '<video src="'+sourceUrl+'" controls="controls" width="100%"/>';
                            editor.replaceSelection(video);
                            var cursor = editor.getCursor();
                            editor.setCursor({
                                line: cursor.line,
                                ch: cursor.ch
                            });
                            editor.focus();
                        } else {
                            layer.alert(result.message);
                        }
                    },
                    error: function () {
                        layer.closeAll();
                    }
                });
            });

            input_f.click();
        },

        uploadImage: function(editor) {
            var input_f = $('<input type="file" name="file" accept="image/jpg,image/jpeg,image/png,image/gif">');
            input_f.on('change', function () {
                var file = input_f[0].files[0];
                if (!file) {
                    return false;
                }
                var form = new FormData();
                form.append("file", file);

                layer.msg('图片上传中...', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                })

                $.ajax({
                    url: _MTONS.BASE_PATH + "/post/upload",
                    data: form,
                    type: "POST",
                    cache: false, //上传文件无需缓存
                    processData: false, //用于对data参数进行序列化处理 这里必须false
                    contentType: false, //必须
                    success: function (result) {
                        layer.closeAll();
                        if (result.status === 200) {
                            var image = '<img src="'+ _MTONS.BASE_PATH + result.path +'" width="100%"/>';
                            editor.replaceSelection(image);
                            var cursor = editor.getCursor();
                            editor.setCursor({
                                line: cursor.line,
                                ch: cursor.ch
                            });
                            editor.focus();
                        } else {
                            layer.alert(result.message);
                        }
                    },
                    error: function () {
                        layer.closeAll();
                    }
                });
            });

            input_f.click();
        },

        setInlineCode: function setInlineCode(editor) {
            editor.replaceSelection(`\`${editor.getSelection()}\``);
            var cursor = editor.getCursor();
            editor.setCursor({
                line: cursor.line,
                ch: cursor.ch - 1
            });
            editor.focus();
        },

        setPreMode: function setPreMode(element, mode, editor) {
			$('button[event=premode].active').removeClass('active');
			element.addClass('active');
			$('.editor-container').removeClass('liveMode editMode previewMode').addClass(mode);
        },
        
        fullscreen: function (editor) {
            var $btn = $('button[event=fullscreen]');
            $btn.toggleClass('active');
            $('.md-editor').toggleClass('fullscreen');
            var height = $(window).height() - 37;
            if ($btn.hasClass('active')) {
                editor.setSize('auto', height + 'px');
            } else {
                editor.setSize('auto', '450px');
            }
        }
    },

    initEditor: function () {
        var editor = CodeMirror.fromTextArea(document.getElementById("content"), {
            mode: 'markdown',     // Markdown
            lineNumbers: true,     // 显示行数
            indentUnit: 4,         // 缩进4格
            tabSize: 4,
            autoCloseBrackets: true,
            matchBrackets: true,   // 括号匹配
            lineWrapping: true,    // 自动换行
            highlightFormatting: true,
            theme: 'idea',
            keyMap: 'sublime',
            extraKeys: {"Enter": "newlineAndIndentContinueMarkdownList"}
        });
        editor.setSize('auto', '450px');

        editor.on('change', function (editor) {
            var $content = $('#content');
            $content.text(editor.getValue());
            $('.editor-preview').html(marked(editor.getValue()));
        });
		
		$('.editor-preview').html(marked($('#content').text()));

        // Toolbar click
        $('div.editor-toolbar').on('click', 'button[event]', function () {
            var that = $(this);
            var event = that.attr('event');
            switch (event) {
                case 'undo':
                    MdEditor.format.undo(editor);
                    break;
                case 'redo':
                    MdEditor.format.redo(editor);
                    break;
                case 'bold':
                    MdEditor.format.setBold(editor);
                    break;
                case 'italic':
                    MdEditor.format.setItalic(editor);
                    break;
                case 'blockquote':
                    MdEditor.format.setBlockQuote(editor);
                    break;
                case 'h1':
                    MdEditor.format.setHeader(editor, 1);
                    break;
                case 'h2':
                    MdEditor.format.setHeader(editor, 2);
                    break;
                case 'h3':
                    MdEditor.format.setHeader(editor, 3);
                    break;
                case 'h4':
                    MdEditor.format.setHeader(editor, 4);
                    break;
                case 'h5':
                    MdEditor.format.setHeader(editor, 5);
                    break;
                case 'link':
                    MdEditor.format.setLink(editor);
                    break;
                case 'image':
                    MdEditor.format.setImage(editor);
                    break;
                case 'font':
                    MdEditor.format.setFont(editor);
                    break;
                case 'center':
                    MdEditor.format.setCenter(editor);
                    break;
                case 'table':
                    MdEditor.format.setTable(editor);
                    break;
                case 'uploadpdf':
                    MdEditor.format.uploadPdf(editor);
                    break;
                case 'uploadzip':
                    MdEditor.format.uploadZip(editor);
                    break;
                case 'uploadexcel':
                    MdEditor.format.uploadExcel(editor);
                    break;
                case 'uploadvideo':
                    MdEditor.format.uploadVideo(editor);
                    break;
                case 'inlinecode':
                    MdEditor.format.setInlineCode(editor);
                    break;
                case 'uploadimage':
                    MdEditor.format.uploadImage(editor);
                    break;
                case 'premode':
					var mode = that.data('value');
                    MdEditor.format.setPreMode(that, mode, editor);
                    break;
                case 'fullscreen':
                    MdEditor.format.fullscreen(editor);
                    break;
                default:
                    break;
            }
        });
    }
};
