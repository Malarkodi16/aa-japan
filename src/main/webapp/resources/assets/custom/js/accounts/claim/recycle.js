var recycle_format_min, recycle_format_max;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });
    $('input.autonumber').autoNumeric('init')
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        let val = $(this).val();
        if (val == 1) {
            $('form#form-received-upload,button#update_amount').removeClass('hidden');
            
        } else {
            $('form#form-received-upload,button#update_amount').addClass('hidden');
        }

        table_recycle.ajax.reload();
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });

    $('#purchaseType').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        var purchasedSupplierEle = $('#purchasedSupplier');
        if (selectedVal.length > 0) {
            $(purchasedSupplierEle).prop('disabled', false);
            $(purchasedSupplierEle).empty();
            var tmpSupplier;
            if (selectedVal == 'auction') {
                tmpSupplier = supplierJson.filter(function(item) {
                    return item.type === 'auction';
                })
                $('#auctionFields').css('display', '')
            } else {
                tmpSupplier = supplierJson.filter(function(item) {
                    return item.type === 'supplier';
                })
                $('#auctionFields').css('display', 'none')
            }
            $(purchasedSupplierEle).select2({
                allowClear: true,
                width: '100%',
                placeholder: 'All',
                data: $.map(tmpSupplier, function(item) {
                    return {
                        id: item.supplierCode,
                        text: item.company
                    };
                })
            })

            $('#purchasedAuctionHouse').select2({
                placeholder: "All"
            })
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
            $('#auctionFields').css('display', 'none')
        }
        var purchaseInfoPosEle = $('#purchasedInfoPos');
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");
        $(purchasedSupplierEle).val('').trigger('change');
        filterPurchaseType = $('#purchaseType').val();
        table_recycle.draw();
    });
    $('#purchasedSupplier').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        var supplier = $(this).find('option:selected').val();
        var purchasedType = $('#purchaseType').find('option:selected').val()
        if ((supplier == null || supplier.length == 0) || $('#purchaseType :selected').val() != 'auction') {
            $('#purchasedAuctionHouse').empty();
            $('#purchasedInfoPos').empty();
            filterSupplierName = supplier;
            table_recycle.draw();
            return;
        }
        filterSupplierName = supplier;
        var auctionHouseArr = [];
        var posNosArr = [];
        $.each(supplierJson, function(i, item) {
            if (item.supplierCode === supplier && item.type === purchasedType) {
                auctionHouseArr = item.supplierLocations;
                //posNosArr=item.
                return false;
            }

        });
        table_recycle.draw();
        $('#purchasedAuctionHouse').empty().select2({
            placeholder: "All",
            allowClear: true,
            data: $.map(auctionHouseArr, function(item) {
                return {
                    id: item.id,
                    text: item.auctionHouse,
                    data: item
                };
            })
        }).val('').trigger("change");

    });
    var filterAuctionHouse, auctionHouseFilterRegex = '';
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).val();
        auctionHouseFilterRegex = ''
        for (var i = 0; i < filterAuctionHouse.length; i++) {
            auctionHouseFilterRegex += "\\b" + filterAuctionHouse[i] + "\\b|";
        }
        auctionHouseFilterRegex = isEmpty(auctionHouseFilterRegex) ? '' : auctionHouseFilterRegex.substring(0, auctionHouseFilterRegex.length - 1);
        table_recycle.draw();
        if ((filterAuctionHouse == null || filterAuctionHouse.length == 0)) {
            $('#purchasedInfoPos').empty();
            return;
        }
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var posNos = data[0].data.posNos;
            $('#purchasedInfoPos').empty().select2({
                placeholder: "All",
                allowClear: true,
                data: $.map(posNos, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger("change");
            if (posNos.length == 1) {
                $('#purchaseInfoPos').val($('#purchaseInfoPos option:eq(0)').val()).trigger('change');

            }

        }
    })
    $('#purchasedInfoPos').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterPosNo = $(this).find('option:selected').val();
        table_recycle.draw();
    });

    /* Date picke function change */

    var recycle_min;
    var recycle_max;
    $('#table-filter-recycle-claim').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        recycle_min = picker.startDate;
        recycle_max = picker.endDate;
        picker.element.val(recycle_min.format('DD-MM-YYYY') + ' - ' + recycle_max.format('DD-MM-YYYY'));
        recycle_min = recycle_min._d.getTime();
        recycle_max = recycle_max._d.getTime();
        recycle_format_min = picker.startDate.format('DD-MM-YYYY');
        recycle_format_max = picker.endDate.format('DD-MM-YYYY');
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_recycle.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        recycle_min = '';
        recycle_max = '';
        table_recycle.draw();
        $('#table-filter-recycle-claim').val('');
        $(this).remove();

    });

    var recycle_received_min;
    var recycle_received_max;
    $('#table-filter-received-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        recycle_received_min = picker.startDate;
        recycle_received_max = picker.endDate;
        picker.element.val(recycle_received_min.format('DD-MM-YYYY') + ' - ' + recycle_received_max.format('DD-MM-YYYY'));
        recycle_received_min = recycle_received_min._d.getTime();
        recycle_received_max = recycle_received_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_recycle.draw();
    });
    $('#receivedClaimdate-form-group').on('click', '.clear-date', function() {
        recycle_received_min = '';
        recycle_received_max = '';
        table_recycle.draw();
        $('#table-filter-received-date').val('');
        $(this).remove();

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table_recycle.search(query, true, false).draw();
    });
     $('#table-filter-length').change(function() {
        table_recycle.page.len($(this).val()).draw();
    });

    //recycle datatable
    let claimStatus;
    var table_recycle_ele = $('#table-claim-recycle');
    var table_recycle = $(table_recycle_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": {
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/documents/recycle/claim/list/datasource",
            data: function(data) {
                var status = $('input[name="radioReceivedFilter"]:checked').val();
                data["status"] = status;
                claimStatus = status;
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox"  value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "width": "50px",
            "data": "purchaseDate"
        }, {
            targets: 2,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            "width": "100px",
            "data": "destCountry"
        }, {
            targets: 4,
            "className": "details-control",
            "width": "100px",
            "data": "destPort"
        }, {
            targets: 5,
            "className": "details-control",
            "width": "100px",
            "data": "vehicleType"
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" name="recycleCost" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycle, '') + '</span>'
                return html;
            }
        }, {
            targets: 7,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                if (claimStatus != 1) {
                    html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimCharge, '') + '</span>'
                } else {
                    html += '<input class="autonumber form-control" name ="claimCharges" form-control" data-a-sign="&yen; " data-m-dec="0" value="' + ifNotValid(row.recycleClaimCharge, '') + '" style="width:100px"/>'
                }
                return html;
            }
        }, {
            targets: 8,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                if (claimStatus != 1) {
                    html += '<span class="autonumber"  data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimInterest, '') + '</span>'
                } else {
                    html += '<input class="autonumber form-control" name ="claimInterest" form-control" data-a-sign="&yen; " data-m-dec="0" value="' + ifNotValid(row.recycleClaimInterest, '') + '" style="width:100px"/>'
                }
                return html;
            }
        }, {
            targets: 9,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                if (claimStatus != 1) {
                    html += '<span class="autonumber" name="recycleReceivedCost" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimReceivedAmount, '') + '</span>'
                } else {
                    html += '<input class="autonumber form-control" name="recycleReceivedCost"  data-a-sign="&yen; " data-m-dec="0" value="' + ifNotValid(row.recycleClaimReceivedAmount, '') + '" style="width:100px"/>'
                }

                return html;
            }
        }, {
            targets: 10,
            "className": "details-control",
            "width": "50px",
            "data": "recycleClaimAppliedDate"
        }, {
            targets: 11,
            "className": "details-control",
            "width": "50px",
            "data": "recycleClaimReceivedDate",
            "render": function(data, type, row) {
                var html = ''
                if (claimStatus != 1) {
                    html += '<span>' + ifNotValid(row.recycleClaimReceivedDate, '') + '</span>'
                } else {
                    html += '<input class="datepicker form-control" name="recycleClaimReceivedDate" style="width:150px"/>'
                }

                return html;
            }
        }, {
            targets: 12,
            'visible': false,
            "data": "recycleClaimStatus",
        }, {
            targets: 13,
            "orderable": false,
            "data": "purchaseInfoType",
            "visible": false //"searchable": false
        }, {
            targets: 14,
            "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 15,
            "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 16,
            "orderable": true,
            "data": "auctionInfoPosNo",
            "visible": false

        }],
        "footerCallback": function(row, data, start, end, display) {
            var table_recycle = this.api();
            updateFooter(table_recycle);
        },
        "drawCallback": function(settings, json) {
            $('span.autonumber,input.autonumber').autoNumeric('init');
            $('input.datepicker').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            })

        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'RecycleClaim_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0, 1, 2, 3, 4, 5, 6, 7],
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
                let currentDate = d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();

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
                    v: 'Recycle Claim'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: !isEmpty(recycle_format_min) ? recycle_format_min + " - " + recycle_format_max : currentDate
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }]

    });
    table_recycle.on('keyup change', 'input[name="claimCharges"],input[name="claimInterest"],input[name="recycleReceivedCost"],input[name="recycleClaimReceivedDate"]', function() {
        let tr = $(this).closest('tr');
        let _tr=table_recycle.row(tr);
        let row = _tr.node();
        var claimCharges = getAutonumericValue($(row).find('input[name="claimCharges"]'));
        var claimInterest = getAutonumericValue($(row).find('input[name="claimInterest"]'));
        var recycleReceivedCost = getAutonumericValue($(row).find('input[name="recycleReceivedCost"]'));
        var recycleClaimReceivedDate = $(row).find('input[name="recycleClaimReceivedDate"]').val();
        if(!isEmpty(claimCharges)||!isEmpty(claimInterest)||!isEmpty(recycleReceivedCost)||!isEmpty(recycleClaimReceivedDate)){            
            $(row).find('td:first-child>input').prop('checked',true)
            _tr.select();
        }else{
            $(row).find('td:first-child>input').prop('checked',false)
            _tr.deselect();
        }

    })
    $('#update_amount').on('click', function(e) {

        let relatedTarget = $(e.relatedTarget);
        var selectedRows = table_recycle.rows({
            selected: true,
            page: 'current'
        })

        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        var selectedRowsData = table_recycle.rows({
            selected: true,
            page: 'current'
        }).nodes();
        var flag = false;
        var pusFlag = false;
        var otherFlag = false;

        var objectArr = [];
        var object = {};
        ;$.each(selectedRowsData, function(index, element) {
            let tr = $(element).closest('tr');
            var chassisNo = $(tr).find('input[name="selRow"]').val();
            var claimCharges = getAutonumericValue($(tr).find('input[name="claimCharges"]'));
            var claimInterest = getAutonumericValue($(tr).find('input[name="claimInterest"]'));
            var recycleReceivedCost = getAutonumericValue($(tr).find('input[name="recycleReceivedCost"]'));
            var recycleClaimReceivedDate = $(tr).find('input[name="recycleClaimReceivedDate"]').val();

            object['chassisNo'] = chassisNo;
            object['claimCharges'] = claimCharges;
            object['claimInterest'] = claimInterest;
            object['recycleReceivedCost'] = recycleReceivedCost;
            object['recycleClaimReceivedDate'] = recycleClaimReceivedDate;

            objectArr.push(object);

        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/documents/recycle/claim/receive",
            contentType: "application/json",
            success: function(objectArr) {
                table_recycle.ajax.reload();
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Record saved.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
            }
        });
    })
    $("#excel_export_all").on("click", function() {
        table_recycle.button("#dt_excel_export_all").trigger();

    });

    //icheck select and deselect
    table_recycle.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_recycle.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_recycle.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_recycle.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_recycle.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_recycle.rows({
            selected: true,
            page: 'current'
        }).count() !== table_recycle.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_recycle.rows({
            selected: true,
            page: 'current'
        }).count() !== table_recycle.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on('click', 'button[name="btn-claim"]', function() {
        var row = table_recycle.row($(this).closest('tr'));
        var data = row.data();
        var response = claimRecycle(data.id);
        if (response.status === 'success') {
            if (!isEmpty(response.data)) {
                row.data(response.data).invalidate();
                table_recycle.draw();
            }
        }
    });
    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();

    // Receivable Received draw dataTable
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (typeof recycle_min != 'undefined' && recycle_min.length != '') {
            if (aData[0].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[0], 'DD-MM-YYYY')._d.getTime();
            }
            if (recycle_min && !isNaN(recycle_min)) {
                if (aData._date < recycle_min) {
                    return false;
                }
            }
            if (recycle_max && !isNaN(recycle_max)) {
                if (aData._date > recycle_max) {
                    return false;
                }
            }
        }
        //purchase type filter
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[9].length == 0 || aData[9] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[10].length == 0 || aData[10] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter

        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[11].length == 0 || !aData[11].match(auctionHouseFilterRegex)) {
                return false;
            }
        }

        ///Pos No filter
        if (typeof filterPosNo != 'undefined' && filterPosNo.length != '') {
            if (aData[12].length == 0 || aData[12] != filterPosNo) {
                return false;
            }
        }

        if (typeof recycle_received_min != 'undefined' && recycle_received_min.length != '') {
            if (aData[7].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[7], 'DD-MM-YYYY')._d.getTime();
            }
            if (recycle_received_min && !isNaN(recycle_received_min)) {
                if (aData._date < recycle_received_min) {
                    return false;
                }
            }
            if (recycle_received_max && !isNaN(recycle_received_max)) {
                if (aData._date > recycle_max) {
                    return false;
                }
            }
        }

        return true;
    });

});
function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);
    $('#count-carTax').html(data.carTax);
    $('#count-insurance').html(data.insurance);
    $('#count-radiation').html(data.radiation);
}
function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // purchase cost total
    var recycleTotal = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="recycleCost"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    // commission amount tax total
    var receivedTotal = table.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="recycleReceivedCost"],input[name="recycleReceivedCost"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
    $('#table-claim-recycle>tfoot>tr.sum').find('span.recycleTotal').autoNumeric('init').autoNumeric('set', recycleTotal);

    $('#table-claim-recycle>tfoot>tr.sum').find('span.receivedTotal').autoNumeric('init').autoNumeric('set', receivedTotal);

}
