var supplierJson;
$(function() {
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
        setReauctionCancelDashboardCount(data.data)
    });
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })
    // price fields autonumeric
    $('.autonumber').autoNumeric('init');
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    // supplier json
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;

    })

    var table = $('#table-re-auction-list').DataTable({

        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": myContextPath + "/stock/re-auction/list/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            //orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "Stock No",
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 1,
            // orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "Chassis No",
            "data": "chassisNo",

        }, {
            targets: 2,
            //   orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "Reauction Date",
            "data": "reauctionDate",

        }, {
            targets: 3,
            // orderable: false,
            "searchable": true,
            "className": "details-control dt-right",
            "name": "Recycle Amount",
            "data": "recycleAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 4,
            //  orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "Auction Company",
            "data": "auctionCompany",

        }, {
            targets: 5,
            //  orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "Auction House",
            "data": "auctionHouse"
        }, {
            targets: 6,
            //   orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "Status",
            "render": function(data, type, row) {
                var reAuctionStatus;
                var className;
                if (row.status == 0) {
                    reAuctionStatus = "Initiated"
                    className = "default"
                } else if (row.status == 1) {
                    reAuctionStatus = "Invoice Generated"
                    className = "info"
                } else if (row.status == 2) {
                    reAuctionStatus = "Sold"
                    className = "success"
                } else if (row.status == 3) {
                    reAuctionStatus = "Cancelled"
                    className = "danger"
                } else if (row.status == 4) {
                    reAuctionStatus = "Sold And Cancelled"
                    className = "danger"
                }
                return '<span class="label label-' + className + '">' + reAuctionStatus + '</span>';
            }
        }, {
            targets: 7,
            orderable: false,
            "name": "Action",
            "render": function(data, type, row) {
                var html = ''
                if (row.status == 0) {
                    html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#re-auctionModal" title="reauction update" data-backdrop="static" data-keyboard="false">Update Invoice Info</button>'
                    html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#cancel-re-auction" title="move to inventory" data-backdrop="static" data-keyboard="false">Move to Inventory</button>'
                    html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#modal-edit-reauction" title="Edit" data-backdrop="static" data-keyboard="false">Edit</button>'
                } else if (row.status == 2) {
                    html += '<button type="button" id="reauctionCancelled" class="btn btn-primary ml-5 btn-xs reauctionCancelled" title="CancelReauction">Cancel Reauction</button>'
                }
                return html;
            }
        }, {
            targets: 8,
            "className": "details-control",
            visible: false,
            "name": "Id",
            "data": "id"
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')
        }

    });

    $('#table-re-auction-list').on('click', '.reauctionCancelled', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "PUT",
            url: myContextPath + "/stock/cancel/re-auction?reauctionId=" + rowData.id,
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
                table.ajax.reload();
            }
        });
        return result;
    })
    var modalTriggerElement;
    $('#re-auctionModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerElement = $(event.relatedTarget);
        var tr = modalTriggerElement.closest('tr');
        var data = table.row(tr).data();
        var id = ifNotValid(data.id, '');
        var itemElement = $('#re-auctionModal').closest('div');
        $(itemElement).find('input[name="id"]').val(id);
        setAutonumericValue($(itemElement).find('input[name="recycleAmount"]'), ifNotValid(data.recycleAmount, 0.0))
        $(this).find('input.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    });

    $("#save-reAuction-details").on('click', function() {
        if (!$('#re-auctionModal form').valid()) {
            return false;
        }
        var object;
        var auctionDetails = $('#re-auctionModal')
        autoNumericSetRawValue($('.autonumber'))
        object = getFormData(auctionDetails.find('input,select'));

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "PUT",
            async: false,
            data: JSON.stringify(object),
            url: myContextPath + "/stock/re-auction/edit",
            contentType: "application/json",
            success: function(data) {
                console.log(data)
                if (data.status === 'success') {
                    var tr = modalTriggerElement.closest('tr');
                    var row = table.row(tr);
                    row.data(data.data.updatedTReAuction).invalidate();
                    $(tr).find('span.autonumber').autoNumeric('init');
                    var alertEle = $('#alert-block');
                    $('#re-auctionModal').modal('toggle');
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    })
    var reAuctionCalcModal = $('#re-auctionModal')
    $(reAuctionCalcModal).on('keyup', ".calcTax", function() {
        var soldPrice = Number($(reAuctionCalcModal).find('input[name="soldPrice"]').autoNumeric('get'));
        var taxAmount = 0.0;
        if (!isEmpty(soldPrice)) {
            var taxAmount = soldPrice * $.i18n.prop('common.tax.percentage');
        }
        $('#tax').autoNumeric('set', taxAmount);

        var shuppinCommission = Number($(reAuctionCalcModal).find('input[name="shuppinCommission"]').autoNumeric('get'));
        var shuppinCommissionTax = 0.0;
        if (!isEmpty(shuppinCommission)) {
            var shuppinCommissionTax = shuppinCommission * $.i18n.prop('common.tax.percentage');
        }
        $('#shuppinTax').autoNumeric('set', shuppinCommissionTax);

        var soldCommission = Number($(reAuctionCalcModal).find('input[name="soldCommission"]').autoNumeric('get'));
        var soldCommissionTax = 0.0;
        if (!isEmpty(soldCommission)) {
            var soldCommissionTax = soldCommission * $.i18n.prop('common.tax.percentage');
        }
        $('#soldCommTax').autoNumeric('set', soldCommissionTax);

        var recycleAmount = Number($(reAuctionCalcModal).find('input[name="recycleAmount"]').autoNumeric('get'));

        let allTotal = soldPrice + taxAmount - shuppinCommission - shuppinCommissionTax - soldCommission - soldCommissionTax + recycleAmount;
        $('#allTotal').autoNumeric('set', allTotal);

    })

    var inventoryModalTriggerElement;
    $('#cancel-re-auction').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        inventoryModalTriggerElement = $(event.relatedTarget);
        var tr = inventoryModalTriggerElement.closest('tr');
        var data = table.row(tr).data();
        $('#cancel-re-auction').find('input[name="id"]').val(ifNotValid(data.id, ''));
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#update-inventory-details', function() {
        if (!$('#cancel-re-auction form').valid()) {
            return false;
        }
        var object;
        var auctionDetails = $('#cancel-re-auction')
        autoNumericSetRawValue($('.autonumber'))
        object = getFormData(auctionDetails.find('input'));

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "PUT",
            async: true,
            data: JSON.stringify(object),
            url: myContextPath + "/stock/re-auction/inventory-details",
            contentType: "application/json",
            success: function(data) {
                //                 console.log(data)
                if (data.status === 'success') {
                    //                     var tr = inventoryModalTriggerElement.closest('tr');
                    //                     var row = table.row(tr);
                    //                     row.data(data.data.updatedTReAuction).invalidate();
                    //                     $('.autonumber').autoNumeric('init');
                    //                     var alertEle = $('#alert-block');
                    table.ajax.reload();
                    $('#cancel-re-auction').modal('toggle');
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    })

    // Re-Auction Modal
    // on open reserve modal
    var modelEle = $('#modal-edit-reauction');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table.row(tr).data();
        $(modelEle).find("#id").val(rowData["id"]);
        $(modelEle).find("#reauctionDate").datepicker('setDate', rowData["reauctionDate"]);
        $(modelEle).find("#stockNo").val(rowData["stockNo"]);
        $(modelEle).find("#chassisNo").val(rowData["chassisNo"]);
        $('#modal-edit-reauction').find('#auctionCompany').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).val(rowData.auctionCompanyId).trigger('change');
        modelEle.find('#auctionHouse').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%'
        }).val(rowData.auctionHouseId).trigger('change');

    }).on('hidden.bs.modal', function() {
        $('#modal-edit-reauction').find('.modal-body').html('')
    }).on('change', '#auctionCompany', function() {
        $(this).closest('.row').find('#auctionHouse').empty()
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {

            $(this).closest('.row').find('#auctionHouse').select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                width: '100%',
                // [0].data.supplierLocations
                data: $.map(data[0].data.supplierLocations, function(item) {
                    return {
                        id: item.id,
                        text: item.auctionHouse

                    };
                })

            }).val('').trigger('change');

        }
    }).on('click', '#btn-edit-reauction', function() {
        if (!$('#modal-edit-reauction form#form-reauction-edit').find('input,select').valid()) {
            return;
        }
        var data = {};
        var object;
        object = getFormData($('#modal-edit-reauction').find('input,select'));
        data = object;
        console.log(data)

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/edit/re-auction",
            async: false,
            contentType: "application/json",
            success: function(data) {
                $('#modal-edit-reauction').modal('toggle');
                table.ajax.reload()
            }
        });
    })

    //stock details modal update
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
})
