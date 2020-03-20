$(function() {
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $('input[name="mobileno"]').inputmask({
        mask: "(99) 9999-999-999"
    });

    $('.select2').select2({
        allowClear: true,
        width: '100%',
    }).val('').trigger('change');

    $.getJSON(myContextPath + "/data/departments.json", function(data) {
        roleJson = data;
        $('#add-User').find('select[name="department"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(roleJson, function(item) {
                return {
                    id: item.department,
                    text: item.department
                };
            })
        }).val('').trigger('change');
    })
    $.getJSON(myContextPath + "/data/mOfficeLocation.json", function(data) {
        officeLocationJson = data;
        $('#add-User').find('select[name="location"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(officeLocationJson, function(item) {
                return {
                    id: item.code,
                    text: item.location
                };
            })
        }).val('').trigger('change');
    })
    //populate role
    $('#add-User').on('change', 'select[name="department"]', function() {
        let department = $(this).val();
        let roles = [];
        if (!isEmpty(department)) {
            roles = roleList(department);
        }
        let role_element = $('#add-User select[name="role"]');
        role_element.empty();
        initRoleOptions(roles, role_element)
        role_element.val('').trigger('change');
        if (department == "SALES") {
            $('#add-User div.sales-person-location').show();
        } else {
            $('#add-User div.sales-person-location').hide();
        }

    })
    var roleList = function(department) {
        let result;
        $.ajax({
            type: "get",
            url: myContextPath + "/data/roles?department=" + department,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    function initRoleOptions(data, element) {
        element.select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.role,
                    text: item.role,
                    data: item
                };
            }),
            templateSelection: function(data, container) {
                // Add custom attributes to the <option> tag for the selected option
                if (!isEmpty(data.data)) {
                    $(data.element).attr('data-hierarchy', data.data.hierarchy);
                    if (data.data.hierarchy != 0) {
                        $(data.element).attr('data-reportto', data.data.reportTo);
                    }

                }

                return data.text;
            }
        })
    }

    $('#add-User').on('change', 'select[name="role"]', function() {
        let reporting_persons_element = $('#add-User select[name="reportTo"]');
        let hierarchy = $(this).find(':selected').attr('data-hierarchy');
        let reportTo = $(this).find(':selected').attr('data-reportto');
        if (isEmpty(hierarchy) || hierarchy == 0) {
            reporting_persons_element.empty().val('').trigger('change');
            $('#add-User div.reporting-person-container').hide();
            return;
        }
        //show reportTo block
        $('#add-User div.reporting-person-container').show();

        let department = $('#add-User select[name="department"]').val();
        let reportingPersons = [];
        if (!isEmpty(department) && !isEmpty(role)) {
            reportingPersons = reportingPersonList(department, reportTo);
        }
        reporting_persons_element.empty().select2({
            allowClear: true,
            width: '100%',
            data: $.map(reportingPersons, function(item) {
                return {
                    id: item.userId,
                    text: item.fullname
                };
            })
        }).val('').trigger('change');
    })
    var reportingPersonList = function(department, role) {
        let result;
        $.ajax({
            type: "get",
            url: myContextPath + "/data/reportingPerson?department=" + department + '&role=' + role,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    $('#add-User').on('hide.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        $(this).find('input[name="username"]').prop('disabled', false);
        $(this).find("input,textarea,select").val('').trigger('change');
    })
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    });

    $('#close_user').on('click', function() {
        $('#user-form').find('input:text').val('')
    });

    var table = $('#table-userlist').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/user/getUser-list",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            orderable: false,
            className: 'select-checkbox',
            targets: 0
        }, {
            targets: 0,
            orderable: false,
            visible: false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "fullname",

        }, {
            targets: 2,
            "data": "dob"
        }, {
            targets: 3,
            "data": "mobileno"
        }, {
            targets: 4,
            "data": "username"
        }, {
            targets: 5,
            "data": "department"
        }, {
            targets: 6,
            "data": "role"
        }, {
            targets: 7,
            "data": "reportToName"
        }, {
            targets: 8,
            "data": "locationName"
        }, {
            targets: 9,
            "data": "id",
            "render": function(data, type, row) {
                var html = '';
                html += '<a type="button" class="btn btn-primary ml-5 btn-xs" data-toggle="modal" data-target="#add-User" name="btnEditUser"><i class="fa fa-fw fa-edit"></i></a>'
                return '<div style="width:100px;">' + html + '</div>';
            }
        }]

    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    //Edit User
    $('table#table-userlist').on('click', 'a[name="btnEditUser"]', function() {
        let rowData = table.row($(this).closest('tr')).data();
        $('#add-User').find('input[name="userId"]').val(rowData.userId);
        $('#add-User').find('input[name="fullname"]').val(rowData.fullname);
        $('#add-User').find('input[name="dob"]').val(rowData.dob);
        $('#add-User').find('input[name="username"]').val(rowData.username).prop('disabled', true);
        $('#add-User').find('input[name="mobileno"]').val(rowData.mobileno);
        $('#add-User').find('#department').val(rowData.department).trigger('change');
        $('#add-User').find('#role').val(rowData.role).trigger('change');
        $('#add-User').find('#reportTo').val(rowData.reportTo).trigger('change');
        $('#add-User').find('#location').val(rowData.location).trigger('change');

    })
    $('#passwordEye').on('click', function() {
        var input = $("#password");
        if (input.attr("type") === "password") {
            input.attr("type", "text");
            $('#passwordEye').find('i.fa-eye').addClass('hidden')
            $('#passwordEye').find('i.fa-eye-slash').removeClass('hidden')
        } else {
            input.attr("type", "password");
            $('#passwordEye').find('i.fa-eye').removeClass('hidden')
            $('#passwordEye').find('i.fa-eye-slash').addClass('hidden')
        }

    })
    $('#save_user').on('click', function() {
        if (!$("#user-form").find('input,select,textarea').valid()) {
            return false;
        }
        let userId = $('#add-User input[name="userId"]').val();
        let type;
        let url;
        if (!isEmpty(userId)) {
            type = 'put';
            url = myContextPath + "/user/edit";
        } else {
            type = 'post';
            url = myContextPath + "/user/create";
        }
        var data = $("#user-form").serialize();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: type,
            data: data,
            url: url,
            async: true,
            dataType: "json",
            success: function(data) {
                table.ajax.reload();
                $('#add-User').modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Customer Saved.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);
                });

            }
        });
    })
})
//System.out.println
