$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    var groupColumn = 2;
    var table = $('#coa-table').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": myContextPath + "/accounts/coa-data",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            targets: 0,
            "data": "code"
        }, {
            targets: 1,
            "data": "account"
        }, {
            targets: 2,
            "data": "subAccount",

        }, {
            targets: 3,
            "data": "reportingCategory",
            "render": function(data, type, row) {
                return data;
            }
        }],

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'CoaList_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0, 1, 2, 3],
            },
            customize: function(xlsx) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                var downrows = 5;
                var clRow = $('row', sheet);
                //update Row
                clRow.each(function() {
                    var attr = $(this).attr('r');
                    var ind = parseInt(attr);
                    ind = ind + downrows;
                    $(this).attr("r", ind);
                });

                // Update  row > c
                $('row c ', sheet).each(function() {
                    var attr = $(this).attr('r');
                    var pre = attr.substring(0, 1);
                    var ind = parseInt(attr.substring(1, attr.length));
                    ind = ind + downrows;
                    $(this).attr("r", pre + ind);
                });

                function Addrow(index, data) {
                    msg = '<row r="' + index + '">'
                    for (i = 0; i < data.length; i++) {
                        var key = data[i].k;
                        var value = data[i].v;
                        msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
                        msg += '<is>';
                        msg += '<t>' + value + '</t>';
                        msg += '</is>';
                        msg += '</c>';
                    }
                    msg += '</row>';
                    return msg;
                }

                var d = new Date();

                //insert
                var r2 = Addrow(2, [{
                    k: 'A',
                    v: 'Company'
                }, {
                    k: 'B',
                    v: 'AA Japan'
                }]);
                var r3 = Addrow(3, [{
                    k: 'A',
                    v: 'Title'
                }, {
                    k: 'B',
                    v: 'Chart of Accounts'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear()
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML =  r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }],
        "order": [[groupColumn, 'asc']],
        "displayLength": 10,
        "initComplete": function(settings, json) {
            $('#coa-table').find('.select2').select2({
                allowClear: true,
                width: '100%'
            });
        },
        //        "drawCallback": function(settings) {
        //            var api = this.api();
        //            var rows = api.rows({
        //                page: 'current'
        //            }).nodes();
        //            var last = null;
        //
        //            api.column(groupColumn, {
        //                page: 'current'
        //            }).data().each(function(group, i) {
        //                if (last !== group) {
        //                    $(rows).eq(i).before('<tr class="group" style="background-color:  aliceblue;"><td colspan="2">' + group + '</td></tr>');
        //
        //                    last = group;
        //                }
        //            });
        //        }

    });

    $("#excel_export_all").on("click", function() {
        $("#groupselect").attr("checked", false);
        $(".selectBox").attr("checked", false);
        //table.rows('.selected').deselect();
        table.button("#dt_excel_export_all").trigger();

    });

    //subaccounttypeJson
    $.getJSON(myContextPath + "/data/reportingcategorytype.json", function(data) {
        var reportingcategorytypeJson = data;
        $('#reportingCategory').select2({
            allowClear: true,
            width: '100%',
            data: $.map(reportingcategorytypeJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        }).val('').trigger("change");
    })

    //reportingcategorytypeJson
    $.getJSON(myContextPath + "/data/reportingcategorytype.json", function(data) {
        var reportingcategorytypeJson = data;
        $('#reportingCategoryType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(reportingcategorytypeJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/data/general-ledger.json", function(data) {
        var generalLedgerJson = data;
        $('#generalLedger').select2({
            allowClear: true,
            width: '100%',
            data: $.map(generalLedgerJson, function(item) {
                return {
                    id: item.ledgerId,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })

    $('#add-new-coa').find('#reportingCategoryType').change(function() {
        var data = {};
        var subAccountData;
        var reportingCategory = $(this).find('option:selected').val();
        $('#coaType').empty();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/search/subAccount?reportingCategory=" + reportingCategory,
            contentType: "application/json",
            async: false,
            success: function(data) {
                subAccountData = data;
                $('#add-new-coa').find('#coaType').select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(subAccountData, function(item) {
                        return {
                            id: item.account,
                            text: item.account,
                            data: item
                        };
                    })

                }).val('').trigger('change')
            }
        });

        if (!isEmpty(subAccountData)) {
            for (var i = 0; i <= subAccountData.length; i++) {
                if (i == subAccountData.length) {
                    var j = i - 1;
                    $('#add-new-coa').find('.modal-title.account-code').removeClass('hidden');
                    $('#add-new-coa').find('.modal-title.account-code>span.code').html(subAccountData[j].code);
                }

            }
        }
    });

    $('#coa-table tbody').on('click', 'tr.group', function() {
        var currentOrder = table.order()[0];
        if (currentOrder[0] === groupColumn && currentOrder[1] === 'asc') {
            table.order([groupColumn, 'desc']).draw();
        } else {
            table.order([groupColumn, 'asc']).draw();
        }
    });

    //Save new COA
    $("#add-new-coa").on('click', '#btn-create-coa', function() {
        if (!$('#create-coa-form').valid()) {
            return;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }

        var objectArr = [];
        var object;
        var data = {};
        $(this).closest('#add-new-coa').find('#cloneTO').each(function(index) {
            object = getFormData($(this).find('input,select,textarea'));
            coaFlag(object);

            objectArr.push(object);
        });

        data['coa'] = object;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/create/coa",
            contentType: "application/json",
            async: false,

            success: function(data) {
                $('#add-new-coa').modal('toggle');
                table.ajax.reload();
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Update Success!</strong> New Tax Type Added Successfully');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);

                });
            }
        });
    });

    $('#add-new-coa').on('hidden.bs.modal', function() {
        $(this).find("input,textarea").val([]);
        $(this).find('select.select2-tag').val('').trigger('change');
    });

    $('.select2-tag').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
        tags: true
    });

    //Filter Search
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (typeof filterType != 'undefined' && filterType.length != '') {
            if (aData[3].length == 0 || aData[3] != filterType) {
                return false;
            }
        }
        return true;
    });

    $('#reportingCategory').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterType = $('#reportingCategory').val();
        table.draw();
    });

});

function coaFlag(object) {
    if (object.reportingCategory == 'Non Current Assets') {
        object.balanceSheet = 1;
        object.reportFlag = 'B';
    } else if (object.reportingCategory == 'Current Assets') {
        object.balanceSheet = 2;
        object.reportFlag = 'B';
    } else if (object.reportingCategory == 'Non Current Liabilities') {
        object.balanceSheet = 3;
        object.reportFlag = 'B';
    } else if (object.reportingCategory == 'Current Liabilities') {
        object.balanceSheet = 4;
        object.reportFlag = 'B';
    } else if (object.reportingCategory == 'Stake Holders Equity') {
        object.balanceSheet = 5;
        object.reportFlag = 'B';
    } else if (object.reportingCategory == 'Income Statement') {
        if (object.account == 'Revenue') {
            object.plOrder = 1;
            object.reportFlag = 'Pl';
        } else if (object.account == 'Cost of Goods Sold') {
            object.plOrder = 2;
            object.reportFlag = 'Pl';
        } else if (object.account == 'Other Income & Gains') {
            object.plOrder = 3;
            object.reportFlag = 'Pl';
        } else if (object.account == 'Selling Expenses') {
            object.plOrder = 4;
            object.reportFlag = 'Pl';
        } else if (object.account == 'Operational Expenses') {
            object.plOrder = 5;
            object.reportFlag = 'Pl';
        } else if (object.account == 'Interest Expenses' || object.account == 'Income Taxes') {
            object.plOrder = 6;
            object.reportFlag = 'Pl';
        }
    }
    return object;
}
