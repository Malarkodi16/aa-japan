$(function() {
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })

    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });
    $(".year-month-picker").inputmask({
        mask: "9999/99"
    });

    //Setting count in dashboard
    setDocumentDashboardStatus();
    var tableEle = $('#table-notReceived');
    var table = $('#table-notReceived').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 1000,
        "ordering": true,

        "ajax": myContextPath + "/documents/tracking/notReceived-list",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: true,
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
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
            "className": "align-center",
            "data": "purchaseInfoDate"
        }, {
            targets: 2,
            "className": "align-center",
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 3,
            "width": "50px",
            "className": "align-center",
            "data": "supplierName"
        }, {
            targets: 4,
            "className": "align-center",
            "data": "auctionHouse"
        }, {
            targets: 5,
            "className": "align-center",
            "data": "shuppinNo",

        }, {
            targets: 6,
            "className": "align-center",
            "visible": false,
            "data": "stockNo",

        }, {
            targets: 7,
            "className": "align-center",
            "data": "sFirstRegDate"
        }, {
            targets: 8,
            "name": "destinationCountry",
            "data": "destinationCountry",
            "visible": true

        }, {
            targets: 9,
            "name": "destinationPort",
            "data": "destinationPort",
            "visible": true

        }, {
            targets: 10,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 11,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 12,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 13,
            "data": "mileage"
        }, {
            targets: 14,
            "data": "documentReceivedDate"
        }, {
            targets: 15,
            "className": "align-center",
            "data": "oldNumberPlate"
        }, {
            targets: 16,
            "className": "align-center",
            "data": "plateNoReceivedDate"
        }, {
            targets: 17,
            "className": "align-center",
            "data": "documentRemarks"
        }, {
            targets: 18,
            "name": "Action",
            "render": function(data, type, row) {
                var html;
                html = '<button type="button" class="ml-5 btn-xs btn btn-primary fa fa-check" data-toggle="modal" data-backdrop="static" data-target="#stock-edit"></button>'
                return html;
            }//"searchable": false
        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.documentStatus == "3") {
                $('td', nRow).css('background-color', '#ef503d');
            }
        }
    });

    $('.datepicker1').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

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
            $('#purchasedInfoPos').empty();
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

    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })
    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterAuctionHouse = $('#purchasedAuctionHouse').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
            }
            if (purchased_min && !isNaN(purchased_min)) {
                if (aData._date < purchased_min) {
                    return false;
                }
            }
            if (purchased_max && !isNaN(purchased_max)) {
                if (aData._date > purchased_max) {
                    return false;
                }
            }

        }
        //purchase type filter
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[12].length == 0 || aData[12] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[13].length == 0 || aData[13] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[14].length == 0 || aData[14] != filterAuctionHouse) {
                return false;
            }
        }
        return true;
    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
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
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on('click', '#documentTrackingNotReceivedSave', function(event) {

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
        data['documentRemarks'] = $(this).closest('tr').find('textarea[name="documentRemarks"]').val();
        var tr = $(this).closest('tr');
        var rowData = table.row(tr).data();
        var row = table.row(tr)
        data['stockNo'] = ifNotValid(rowData.stockNo, '');
        data['id'] = ifNotValid(rowData.id, '');
        var response = updateReceivedDetails(data);
        if (response.status === 'success') {
            row.data(response.data).invalidate();
            setDocumentDashboardStatus();
        }
    });

    var modalUpdateDocType = $('#modal-update-docType')
    var selectedDocType = null;
    $(modalUpdateDocType).find('select[name="docType"]').on('change', function() {
        selectedDocType = $(this).find('option:selected').val();
        if (selectedDocType == 0) {
            $(modalUpdateDocType).find('#docConvertWithMasho').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertWithoutMasho').removeClass('hidden')
        } else {
            $(modalUpdateDocType).find('#docConvertWithMasho').removeClass('hidden')
            $(modalUpdateDocType).find('#docConvertWithoutMasho').addClass('hidden')
        }

    })
    $(modalUpdateDocType).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        $(modalUpdateDocType).find('#docConvertWithoutMasho').addClass('hidden')
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.documentRemarks)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }

        var headerEle = $('#modal-update-docType').find('.modal-header');
        headerEle.find('#count').html(data.length);
        $('#modal-update-docType').find('.modal-body').find('textarea[name="documentRemarks"]').val(ifNotValid(data[0], ''))

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#update-doc', function(e) {
        if (!$('#docType-update').valid() || (!confirm($.i18n.prop('common.confirm.update.docType')))) {
            return;
        }
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })

        var docType = $('#modal-update-docType').find('#docType').val();
        var date = $("#modal-update-docType").find('#doc-received-date').val();
        if (selectedDocType == 0) {
            var documentConvertTo = $(modalUpdateDocType).find('#docConvertWithoutMasho').find('#documentConvertTo').val();
        } else {
            var documentConvertTo = $(modalUpdateDocType).find('#docConvertWithMasho').find('#documentConvertTo').val();
        }
        //var documentRemarks = $("#modal-update-docType").find('#documentRemarks').val();
        var parm = '?docType=' + docType + '&date=' + date + '&documentConvertTo=' + documentConvertTo
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/updateDocType" + parm,
            async: false,
            contentType: "application/json",
            success: function(data) {
                setDocumentDashboardStatus();
                table.ajax.reload();
                $('#modal-update-docType').modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock Updated.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);

                });

            }
        });
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

    $('#stock-edit').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var rowData = table.row($(e.relatedTarget).closest('tr')).data()
        var stockNo = rowData.stockNo;
        var firstRegDate = ifNotValid(rowData.sFirstRegDate, '');
        var chassisNo = rowData.chassisNo;
        var plateNo = ifNotValid(rowData.oldNumberPlate, '');
        var plateNoReceivedDate = ifNotValid(rowData.plateNoReceivedDate, '');
        var mileage = rowData.mileage;
        var documentRemarks = rowData.documentRemarks;
        var editChassisNo = $('#editChassisNo').is(':checked');

        if (isEmpty(rowData.numberPlate) || rowData.numberPlate == "no" || editChassisNo) {
            $(this).find('input[name="oldNumberPlate"]').attr('readonly', false);
            $(this).find('input[name="oldNumberPlate"]').removeClass('required');
            $(this).find('input[name="oldNumberPlate"]').parent().find('label').removeClass('required');
            $(this).find('input[name="plateNoReceivedDate"]').val(ifNotValid(plateNoReceivedDate, '')).attr('readonly', false);
            //$(this).find('input[name="plateNoReceivedDate"]').removeClass('required');
            //$(this).find('input[name="plateNoReceivedDate"]').parent().find('label').removeClass('required');
        } else {
            $(this).find('input[name="oldNumberPlate"]').attr('readonly', false);
            $(this).find('input[name="oldNumberPlate"]').addClass('required');
            $(this).find('input[name="oldNumberPlate"]').parent().find('label').addClass('required');
            var dateMomentConv = moment(plateNoReceivedDate, 'DD-MM-YYYY hh:mm');
            $(this).find('input[name="plateNoReceivedDate"]').datepicker("setDate", dateMomentConv.toDate()).attr('readonly', false);
            //$(this).find('input[name="plateNoReceivedDate"]').addClass('required');
            //$(this).find('input[name="plateNoReceivedDate"]').parent().find('label').addClass('required');
        }
        $('#stock-stockNo').val(stockNo);
        //         $('#plateStat').val(plateStat);
        $(this).find('input[name="sFirstRegDate"]').val(firstRegDate)
        $(this).find('input[name="oldNumberPlate"]').val(plateNo)
        $(this).find('input[name="mileage"]').val(mileage)
        $(this).find('input[name="chassisNo"]').val(chassisNo)
        $(this).find('input[name="plateNoReceivedDate"]').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        })
        if (!isEmpty(plateNoReceivedDate)) {
            var dateMomentConv = moment(plateNoReceivedDate, 'DD-MM-YYYY hh:mm');
            $(this).find('input[name="plateNoReceivedDate"]').datepicker('setDate', dateMomentConv.toDate());
        }
        if (isEmpty(rowData.numberPlate) || rowData.numberPlate == "no") {
            $(this).find('input[name="plateNoReceivedDate"]').prop('disabled', false).datepicker('disable');
        }
        //         else {
        //             
        //         }

        $(this).find('textarea[name="documentRemarks"]').val(documentRemarks);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $(this).find('input[name="plateNoReceivedDate"]').prop('disabled', false)
    }).on('click', '.editChassisNo', function(e) {
        var plateStat = $('#plateStat').val();
        if ($(this).is(':checked')) {
            $('#stock-edit').find('input[name="oldNumberPlate"]').attr('readonly', true);
            $('#stock-edit').find('input[name="oldNumberPlate"]').removeClass('required');
            $('#stock-edit').find('input[name="oldNumberPlate"]').parent().find('label').removeClass('required');
            $('#stock-edit').find('input[name="plateNoReceivedDate"]').attr('readonly', true);
            $('#stock-edit').find('input[name="plateNoReceivedDate"]').datepicker("destroy");
            //$('#stock-edit').find('input[name="plateNoReceivedDate"]').removeClass('required');
            //$('#stock-edit').find('input[name="plateNoReceivedDate"]').parent().find('label').removeClass('required');
        } else {
            if (isEmpty(plateStat) || plateStat == "no")
                 $('#stock-edit').find('input[name="oldNumberPlate"]').attr('readonly', false);
            else
                $('#stock-edit').find('input[name="oldNumberPlate"]').attr('readonly', false);
                
            $('#stock-edit').find('input[name="oldNumberPlate"]').addClass('required');
            $('#stock-edit').find('input[name="oldNumberPlate"]').parent().find('label').addClass('required');
            $('#stock-edit').find('input[name="plateNoReceivedDate"]').attr('readonly', false)
            $('#stock-edit').find('input[name="plateNoReceivedDate"]').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        })
            //$('#stock-edit').find('input[name="plateNoReceivedDate"]').addClass('required');
            // $('#stock-edit').find('input[name="plateNoReceivedDate"]').parent().find('label').addClass('required');
        }
    }).on('click', '#update-stock-detail', function() {
        if (!$('#stock-edit-detail').valid()) {
            alert($.i18n.prop('common.alert.validation.required'));
            return;
        }
        if (!confirm($.i18n.prop('confirm.stock.edit'))) {
            return;
        }
        var tableData = $(this).closest('tr')
        var object = getFormData($(this).closest('.modal-content').find('input,textarea'));
        if ($('#editChassisNo').is(':checked')) {
            updateChassisNo(object)
        } else {
            updateReceivedDetails(object)
        }

        $('#stock-edit').modal('toggle');
        location.reload();
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
        url: myContextPath + "/documents/tracking/updatePlateNo",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function updateChassisNo(data) {
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
        url: myContextPath + "/documents/tracking/updateChassisNo",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function isEntryValid(element) {
    var valid = true;
    valid = $(element).find('input[name="oldNumberPlate"],input[name="documentRemarks"]').valid();
    return valid;
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
function checkChassisNo(element) {
    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        url: myContextPath + "/check/existing/chassisNo",
        type: "get",
        cache: false,
        dataType: "json",
        async: false,
        data: {
            stockNo: function() {
                return $('#stock-stockNo').val()
            },
            chassisNo: function() {
                return element
            }
        },
        success: function(data) {
            response = data;
        }

    })
    return response;

}
