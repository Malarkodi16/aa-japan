$(function() {
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });

    //Radio Button with Container 
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            $('#buttonShowHide').find('#takeToRikuji').removeClass('hidden');
            $('#buttonShowHide').find('#updateDocumentFromRikuji').addClass('hidden');
            table.ajax.reload()
        } else if ($(this).val() == 1) {
            $('#buttonShowHide').find('#takeToRikuji').addClass('hidden');
            $('#buttonShowHide').find('#updateDocumentFromRikuji').removeClass('hidden');
            table.ajax.reload();
        }
    });
    //Setting count in dashboard
    setDocumentDashboardStatus();
    //Document Type
    $('.documentType').select2({
        allowClear: true,
        width: "100%"
    })
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
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
            $('#auctionFields').css('display', 'none')
        }
        var purchaseInfoPosEle = $('#purchasedInfoPos');
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");
        $(purchasedSupplierEle).val('').trigger('change');
        filterPurchaseType = $('#purchaseType').val();
        table.draw();
    });

    $('#purchasedSupplier').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        var supplier = $(this).find('option:selected').val();
        var purchasedType = $('#purchaseType').find('option:selected').val()
        if ((supplier == null || supplier.length == 0) || $('#purchaseType :selected').val() != 'auction') {
            $('#purchasedAuctionHouse').empty();
            filterSupplierName = supplier;
            table.draw();
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
        table.draw();
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
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).find('option:selected').val();
        table.draw();

    })

    var purchase_date_min;
    var purchase_date_max;
    $('#table-filter-document-received-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchase_date_min = picker.startDate;
        purchase_date_max = picker.endDate;
        picker.element.val(purchase_date_min.format('DD-MM-YYYY') + ' - ' + purchase_date_max.format('DD-MM-YYYY'));
        purchase_date_min = purchase_date_min._d.getTime();
        purchase_date_max = purchase_date_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date-arrival').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date-arrival'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-form-group-arrival').on('click', '.clear-date-arrival', function() {
        purchase_date_min = '';
        purchase_date_max = '';
        table.draw();
        $('#table-filter-request-from-sales-arrival-date').val('');
        $(this).remove();

    })

    /* Received Data-table*/

    var table_received_list_Ele = $('#table-received-list');
    var table = table_received_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 200,
        "ordering": true,
        "aaSorting": [[0, "desc"]],

        "ajax": {
            'url': myContextPath + "/documents/tracking/received-list",
            'data': function(data) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                data["show"] = selected;
                return data;
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
            "orderable": false,
            className: 'select-checkbox',
            "data": "id",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" id="check-box-select" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">' + '<input type="hidden" name="stockNo" value="' + row.stockNo + '"/>' + '<input type="hidden" name="invoiceType" value="' + row.purchaseType + '"/>' + '<input type="hidden" name="invoiceName" value="' + row.supplier + '"/>' + '<input type="hidden" name="supplierCode" value="' + row.supplierCode + '"/>' + '<input type="hidden" name="supplierName" value="' + row.supplierName + '"/>' + '<input type="hidden" name="model" value="' + row.model + '"/>' + '<input type="hidden" name="maker" value="' + row.maker + '"/>';

                }
                return data;
            }
        }, {
            targets: 1,
            "name": "Chassis No",
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 2,
            "name": "Received Doc type",
            "render": function(data, type, row) {
                var data;
                if (row.documentType == 0) {
                    data = "MASHO"
                } else if (row.documentType == 1) {
                    data = "SHAKEN"
                }
                return data
            }
        }, {
            targets: 3,
            "className" : "dt-right",
            "visible": true,
            "orderable": false,
            "data": "documentFob",
            "render": function(data, type, row) {
                return '<input type="text" name="documentFob" data-a-sign="¥ " data-v-min="0" data-m-dec="0" class="form-control pull-right autonumber" value="' + ifNotValid(data, '') + '" style=" width: 120px;">';

            }

        }, {
            targets: 4,
            "name": "Doc Received Date",
            "data": "documentReceivedDate"
        }, {
            targets: 5,
            "name": "Plate No",
            "data": "oldNumberPlate",
            "width": "100px",
            "render": function(data, type, row) {
                var html = '';
                if (row.documentType == 0) {
                    html = '<input type="text" name="oldNumberPlate" class="form-control pull-right" readonly="readonly" value="' + ifNotValid(data, '') + '" style=" width: 120px;">'
                } else {
                    html = '<input type="text" name="oldNumberPlate" class="form-control pull-right" value="' + ifNotValid(data, '') + '" style=" width: 120px;">'
                }
                //else {
                //html = '<input type="text" name="oldNumberPlate" class="form-control pull-right" style=" width: 120px;">'
                //}
                return html;

            }
        }, {
            targets: 6,
            "name": "Plate No Received Date",
            "data": "plateNoReceivedDate",
            "width": "100px",
            "render": function(data, type, row) {
                var html = '';
                if (row.documentType == 0) {
                    html = '<div class="input-group date"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" class="form-control pull-right datepicker" name="plateNoReceivedDate" value="' + ifNotValid(data, '') + '"id="datepicker" disabled="disabled" style=" width: 120px; "></div>'
                } else {
                    html = '<div class="input-group date"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" class="form-control pull-right datepicker" name="plateNoReceivedDate" value="' + ifNotValid(data, '') + '" id="datepicker" style=" width: 120px; "></div>'
                }
                return html;

            }
        }, {
            targets: 7,
            "name": "Convert To",
            "data": "documentConvertTo",
            "width": "100px",
            "render": function(data, type, row) {
                var html;
                if (row.documentType == 0) {
                    html = '<div class="form-group"><select name="documentConvertTo" data-value="' + ifNotValid(data, '') + '" id="documentConvertTo" class="form-control select2-select documentType" data-placeholder="Convert To"><option value="1">Export Certificate</option> <option value="2">Name Transfer</option> <option value="3">Domestic</option><option value="5">Parts</option> <option value="6">SHUPPIN</option> </select> </div>'
                } else if (row.documentType == 1) {
                    html = '<div class="form-group"><select name="documentConvertTo" data-value="' + ifNotValid(data, '') + '" id="documentConvertTo" class="form-control select2-select documentType" data-placeholder="Convert To"><option value="1">Export Certificate</option> <option value="2">Name Transfer</option> <option value="3">Domestic</option> <option value="4">Masho</option> <option value="5">Parts</option> <option value="6">SHUPPIN</option> </select> </div>'
                }
                return html;
            }
        }, {
            targets: 8,
            "name": "Converted Date",
            "data": "documentConvertedDate",
            "width": "130px",
            "render": function(data, type, row) {
                return html = '<div class="input-group date"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" class="form-control pull-right datepicker" name="documentConvertedDate" value="' + ifNotValid(data, '') + '" id="datepicker" style=" width: 130px; "></div>'
            }
        }, {
            targets: 9,
            "name": "Action",
            "render": function(data, type, row) {
                return '<button type="button" class="ml-5 btn-xs btn btn-primary fa fa-save" id="documentTrackingReceivedSave"></button><button type="button" class="ml-5 btn-xs btn btn-danger fa fa-close" data-toggle="modal" data-target="#modal-remark" data-backdrop="static" data-keyboard="false" id="deleteButton"></button>';
            }
        }, {
            targets: 10,
            "name": "Purchased Date",
            "visible": false,
            "width": "70px",
            "render": function(data, type, row) {
                return ifNotValid(row.purchaseInfoDate, '');
            }
        }, {
            targets: 11,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 12,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 13,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }],

        "fnDrawCallback": function(oSettings) {
            $('.datepicker').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            })
            $('#table-received-list select[name="documentConvertTo"]').each(function() {
                $(this).select2({
                    placeholder: function() {
                        return $(this).attr('data-value')
                    },
                    width: '120px',
                    allowClear: true,
                }).val($(this).attr('data-value')).trigger('change');
            })
            table_received_list_Ele.find('input.autonumber').autoNumeric('init')
        }

    });

    $('.datepicker1').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true /*,
        defaultDate: new Date()*/
    }).on('change', function() {
        $(this).valid();

    })

    //Received Table Checkbox Property
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
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);
        }
    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);
        }
    }).on('click', '#documentTrackingReceivedSave', function(event) {

        var tableData = $(this).closest('tr');
        if (!isEntryValid($(tableData))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var data = {};
        data['oldNumberPlate'] = $(this).closest('tr').find('input[name="oldNumberPlate"]').val();
        data['plateNoReceivedDate'] = $(this).closest('tr').find('input[name="plateNoReceivedDate"]').val();
        data['documentConvertedDate'] = $(this).closest('tr').find('input[name="documentConvertedDate"]').val();
        data['documentConvertTo'] = $(this).closest('tr').find('select[name="documentConvertTo"]').val();
        var rowData = table.row($(this).closest('tr')).data();
        var closestTr = $(this).closest('tr')
        var row = table.row(closestTr)

        var selected = $('input[name="radioShowTable"]:checked').val();
        data["show"] = selected;
        data['stockNo'] = ifNotValid(rowData.stockNo, '');
        data['id'] = ifNotValid(rowData.id, '');
        var response = updateReceivedDetails(data);
        if (response.status === 'success') {
            setDocumentDashboardStatus();
            row.data(response.data).invalidate();

            closestTr.find('select[name="documentConvertTo"]').select2({
                placeholder: function() {
                    return $(this).attr('data-value')
                },
                width: '120px',
                allowClear: true,
            }).val(closestTr.find('select[name="documentConvertTo"]').attr('data-value')).trigger('change');
            closestTr.find('input[name="documentConvertedDate"],input[name="plateNoReceivedDate"]').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            });
            closestTr.find('input.autonumber').autoNumeric('init')
        }
    })

    var rikujiModalEle = $('#modal-rikuji-remark')
    $(rikujiModalEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        $(rikujiModalEle).find('input[name="rikujiUpdateToOneDate"]').datepicker("setDate", new Date());
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-rikuji-remark-submit', function(event) {

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        var rikujiUpdateToOneDate = $(rikujiModalEle).find('input[name="rikujiUpdateToOneDate"]').val();
        var rikujiRemarks = $(rikujiModalEle).find('textarea[name="rikujiRemarks"]').val();
        var param = "?rikujiUpdateToOneDate=" + rikujiUpdateToOneDate + "&rikujiRemarks=" + rikujiRemarks + "&flag=" + "RECEIVED"
        var response = updateRikujiTo1(data, param)
        if (response.status === 'success') {
            $(rikujiModalEle).modal('toggle');
            table.ajax.reload();
            setDocumentDashboardStatus();
        }
    });

    $('#updateDocumentFromRikuji').on('click', function(e) {

        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var row = table.row(this)
            if (!isEntryValid($(row.node()))) {
                alert($.i18n.prop('common.alert.validation.required'));
                return;
            }
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var response = updateDocumentFromRikuji(data)
        if (response.status === 'success') {

            table.ajax.reload();
            setDocumentDashboardStatus();
        }
    })

    //update transportation status
    var statusUpdateEle;
    var rowDataRemarkStockNo;
    $('#modal-remark').on('show.bs.modal', function(event) {
        statusUpdateEle = $(event.relatedTarget);
        var tr = $(statusUpdateEle).closest('tr');
        var row = table.row(tr);
        var rowData = row.data();
        rowDataRemarkStockNo = ifNotValid(rowData.stockNo, '')
        $(this).find('textarea[name="documentRemarks"]').val(ifNotValid(rowData.documentRemarks, ''));
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-remark-submit', function(event) {

        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return false;
        }
        var data = {};
        data["documentRemarks"] = $('#modal-remark').find('textarea[name="documentRemarks"]').val();
        data["stockNo"] = rowDataRemarkStockNo
        var tr = $(statusUpdateEle).closest('tr');
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/receivedDoc/cancel",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-remark').modal('toggle');
                    setDocumentDashboardStatus();
                    table.row(tr).remove().draw();
                }
            }
        });
    })

    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterAuctionHouse = $('#purchasedAuctionHouse').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (oSettings.sTableId == 'table-received-list') {
            if (typeof purchase_date_min != 'undefined' && purchase_date_min.length != '') {
                if (aData[9].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[9], 'DD-MM-YYYY')._d.getTime();
                }
                if (purchase_date_min && !isNaN(purchase_date_min)) {
                    if (aData._date < purchase_date_min) {
                        return false;
                    }
                }
                if (purchase_date_max && !isNaN(purchase_date_max)) {
                    if (aData._date > purchase_date_max) {
                        return false;
                    }
                }

            }
            if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
                if (aData[11].length == 0 || aData[11] != filterPurchaseType) {
                    return false;
                }
            }
            //Supplier filter
            if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
                if (aData[12].length == 0 || aData[12] != filterSupplierName) {
                    return false;
                }
            }

            //Auction Housee filter
            if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
                if (aData[13].length == 0 || aData[13] != filterAuctionHouse) {
                    return false;
                }
            }

        }
        return true;
    })

    //     Stock Modal
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: ''
    });
    stockDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        stockCloneElement.clone().appendTo(stockDetailsModalBody);
        //updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
    })

    $('#save-fob-costs').on('click', function(e) {

        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        });
        var selectedRowsData = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var rowDatas = selectedRows.data();
        if (!$('#receivedForm').valid() || !isFobEntryValid($(selectedRows.nodes()))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return e.preventDefault();
        } else {//$(this).closest('tr').find('#check-box-select');
        }

        autoNumericSetRawValue($(selectedRowsData).find('input.autonumber'))
        var objectArr = [];
        var object;
        var data = {};
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = $(this.node());
            object = getFormData($(data).find('input'));
            objectArr.push(object);
        });
        data['fobPriceList'] = objectArr;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/update/fob-price",
            contentType: "application/json",
            success: function(data) {
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Stock saved.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                    table.ajax.reload();
                });

            }
        });
    })
})

function updateReceivedDetails(data) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "put",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/update/received",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function updateRikujiTo1(data, param) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "put",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/updateRikujiStatus1" + param,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function updateDocumentFromRikuji(data) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "put",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/updateDocumentFromRikuji",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function isEntryValid(element) {
    var valid = true;
    valid = $(element).find('select[name="documentConvertTo"], input[name="documentConvertedDate"]').valid();

    return valid;
}
function isFobEntryValid(element) {
    var valid = true;
    //     valid = $(element).find('input[name="purchaseCost"],input[name="commision"],input[name="recycle"]').valid();
    valid = elementValueNotEmpty($(element).find('input[name="documentFob"]'));
    return valid;
}
var elementValueNotEmpty = function(elements) {
    for (var i = 0; i < elements.length; i++) {
        if (isEmpty($(elements[i]).val())) {
            return false;
        }
    }
    return true;
}

function setDocumentDashboardStatus() {
    $.getJSON(myContextPath + "/data/documents-dashboard/data-count", function(data) {
        $('#notReceived-count').html(data.data.notReceived);
        $('#received-count').html(data.data.received + "/" + data.data.receivedRikuji);
        $('#certificate-count').html(data.data.exportCertificate);
        $('#nameTransfer-count').html(data.data.nameTransfer + "/" + data.data.domestic + "/" + data.data.parts + "/" + data.data.shuppin);
        //$('#domestic-count').html(data.data.domestic);
    });
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
    	$('#cancel-stock').html(data.data.cancelStock);
    });
}
