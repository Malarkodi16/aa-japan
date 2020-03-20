var mobileNo, city;
$(function() {
    //customer filter
    $('#custId').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/getCustomerNotApproved",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    });
    var tableEle = $('#table-customerlist');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/accounts/customerlist-data",

        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            orderable: true,
        }, {
            targets: 0,
            "data": "code"
        }, {
            targets: 1,
            "data": "firstName",

        }, {
            targets: 2,
            "data": "lastName",
            visible: false
        }, {
            targets: 3,
            "data": "nickName"
        }, {
            targets: 4,
            "data": "email"
        }, {
            targets: 5,
            "data": "skypeId",
            "visible": false
        }, {
            targets: 6,
            "data": "mobileNo"
        }, {
            targets: 7,
            "data": "companyName",
            "visible": true
        }, {
            targets: 8,
            "className": "dt-right",
            "data": "creditBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 9,
            "data": "country"
        }, {
            targets: 10,
            "data": "port",
            "visible": true
        }, {
            targets: 11,
            "data": 'null',
            "className": 'align-center',
            "width": 50,
            "render": function(data, type, row) {
                var html = ''
                html = '<a type="button" class="btn btn-primary approve-customer  btn-xs" id="approve-customer"><i class="fa fa-fw fa-check"></i></a>'
                return html;
            }
        }, {
            targets: 12,
            "data": "city",
            "visible": false
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')

        }
    });

    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(table).find("th.select-checkbox>input").removeClass("selected");
            $(table).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table).find("th.select-checkbox>input").addClass("selected");
            $(table).find("th.select-checkbox>input").prop('checked', true);
        }
    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(table).find("th.select-checkbox>input").removeClass("selected");
            $(table).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table).find("th.select-checkbox>input").addClass("selected");
            $(table).find("th.select-checkbox>input").prop('checked', true);
        }
    });
    //Filter with Customer
    var fName;
    $('.customer').change(function() {
        fName = $(this).val();
        table.draw();

    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //orgin port filter

        if (typeof fName != 'undefined' && fName.length != '') {
            if (aData[0].length == 0 || aData[0] != fName) {
                return false;
            }
        }
        if (oSettings.sTableId == "table-customerlist") {
            if (!(isEmpty(mobileNo) && isEmpty(city))) {
                if ((aData[6].length == 0 || aData[6] != mobileNo) || (aData[11].length == 0 || aData[11] != city)) {
                    return false;
                }
            }
        }
        return true;
    })
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};

    //Table Global Search
    $('#table-filter-search').keyup(function() {
         var query = regex_escape($(this).val());
        table.search(query, true, false).draw();

    });
    //Table Length
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $('#table-customerlist').on('click', '.approve-customer', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        var params = '?customerCode=' + rowData.code
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            url: myContextPath + "/accounts/approve-customer" + params,
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
                table.ajax.reload();
            }
        });
        return result;
    })

})
