$(function() {
    let userDropdown = $('#user');
    let tree = $("#tree");
    let selected = [];
    $.getJSON(myContextPath + "/data/findAllUsers", function(data) {
        userDropdown.select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.fullname
                };
            })
        }).val('').trigger("change");
    });

    tree.jstree({
        'core': {
            'check_callback': true,
            'data': []
        },
        "checkbox": {
            'deselect_all': true,
            'three_state': false,
            "keep_selected_style": false
        },
        "plugins": ["checkbox"]
    }).bind("ready.jstree", function(event, data) {
        $(this).jstree("open_all");
    }).on('changed.jstree', function(e, data) {
        $(this).jstree("open_all");
    }).on("refresh.jstree", function(e) {
        tree.jstree("deselect_all")
        tree.jstree('select_node', selected);
    })

    userDropdown.on('change', function() {
        let userId = $(this).val();
        if (!isEmpty(userId)) {
            createAccessTree(userId)
        } else {
            tree.jstree(true).settings.core.data = [];
            tree.jstree('refresh')

        }
    })
    $('button#update').on('click', function() {
        let userId = userDropdown.val();
        if (!isEmpty(userId)) {

            let data = {};
            let selected = tree.jstree('get_selected');
            data.access = selected;
            data.userId = userId;
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "post",
                data: JSON.stringify(data),
                url: myContextPath + "/accounts/uac/update/access",
                contentType: "application/json",
                async: false,
                success: function(data) {
                    if (data.statu = 'success') {
                        var alertEle = $('#alert-block');
                        $(alertEle).css('display', '').html('<strong>Success!</strong> Access Updated');
                        $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                            $(alertEle).slideUp(500);
                        });
                    }

                }
            });
        }
    })
    function createAccessTree(userId) {
        $.ajax({
            async: false,
            type: "GET",
            url: myContextPath + "/accounts/uac/data/" + userId,
            dataType: "json",
            success: function(json) {
                tree.jstree(true).settings.core.data = json.data;
                tree.jstree('refresh')

                selected = json.access
            }
        });
    }

});
