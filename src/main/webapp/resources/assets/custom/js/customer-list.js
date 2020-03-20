var mobileNo, city;
$(function() {
    mobileNo = $('#request-from-customer-filter-mobile-no').val()
    city = $('#request-from-customer-filter-city').val()
    //customer filter
    $('#custId').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/admin/search",
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
    var table = $('#table-customerlist').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/sales/customerlist-data",
            data: function(data) {
                let isChecked = $('#showAll').is(":checked");
                data.showAll = isChecked ? 1 : 0
            },
        },

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
            "data": "country"
        }, {
            targets: 9,
            "data": "port",
            "visible": true
        }, {
            targets: 10,
            orderable: false,
            "data": 'null',
            "className": 'align-center',
            "width": 50,
            "render": function(data, type, row) {
                var html = ''
                html = '<a href="' + myContextPath + '/customer/' + row.code + '" class="btn btn-primary btn-xs"><i class="fa fa-edit" title="Edit"></i></a>'
                html += '<a href="' + myContextPath + '/customer/transaction/' + row.code + '" class="btn btn-success btn-xs ml-5"><i class="fa fa-handshake-o" title="Customer Transaction"></i></a>'
                return html;
            }
        }, {
            targets: 11,
            "data": "city",
            "visible": false
        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.approveCustomerflag == 0) {
                $('td', nRow).css('background-color', '#ef503d');
            }
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

    $('#showAll').on('click', function() {
        table.ajax.reload();

    })

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

    //Table Global Search
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();

    });
    //Table Length
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

})
