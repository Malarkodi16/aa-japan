var carTaxApproveJson;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    //         $('input.autonumber').autoNumeric('init')
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            table_carTax.column(5).visible(false);
            table_carTax.column(6).visible(false);
            $('button.claim-button').show();
        } else if ($(this).val() == 2) {
            table_carTax.column(5).visible(true);
            table_carTax.column(6).visible(true);
            $('button.claim-button').hide();
        }
        table_carTax.draw();
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
        table_carTax.draw();
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
            table_carTax.draw();
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
        table_carTax.draw();
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
        table_carTax.draw();
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
        table_carTax.draw();
    });

    /* Date picke function change */

    var carTax_min;
    var carTax_max;
    $('#table-filter-carTax-claim').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        carTax_min = picker.startDate;
        carTax_max = picker.endDate;
        picker.element.val(carTax_min.format('DD-MM-YYYY') + ' - ' + carTax_max.format('DD-MM-YYYY'));
        carTax_min = carTax_min._d.getTime();
        carTax_max = carTax_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_carTax.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        carTax_min = '';
        carTax_max = '';
        table_carTax.draw();
        $('#table-filter-carTax-claim').val('');
        $(this).remove();

    });

    var table_carTax_ele = $('#table-claim-carTax');
    var table_carTax = $(table_carTax_ele).DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/claim/cartax/list/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "stockNo",
            "width": "5px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "width": "50px",
            "data": "stockNo"
        }, {
            targets: 2,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets: 3,
            "width": "100px",
            "className": "details-control",
            "data": "purchaseDate",
        }, {
            targets: 4,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" name="roadTax" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.roadTax, 0) + '</span>'
                return html;
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "width": "100px",
            'visible': false,
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" name="carTaxClaimReceivedAmount" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.carTaxClaimReceivedAmount, '') + '</span>'
                return html;
            }
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "width": "100px",
            'visible': false,
            "data": "receivedDate"
            
        }, {
            targets: 7,
            "width": "100px",
            "className": "details-control",
            "data": "carTaxClaimStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = '';
                    var className = "label-default";
                    if (data == 0) {
                        status = 'Not Claimed';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'Claimed';
                        className = "label-info";
                    } else if (data == "2") {
                        status = "Amount Received"
                        className = "label-success"

                    }
                    return '<span class="label ' + className + '" style="width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 8,
            'visible': false,
            "data": "carTaxClaimStatus",
        }, {
            targets: 9,
            "orderable": false,
            "data": "purchaseInfoType",
            "visible": false //"searchable": false
        }, {
            targets: 10,
            "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 11,
            "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 12,
            "orderable": true,
            "data": "auctionInfoPosNo",
            "visible": false

        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')

        }

    });

    //icheck select and deselect
    table_carTax.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_carTax.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_carTax.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_carTax.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_carTax.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_carTax.rows({
            selected: true,
            page: 'current'
        }).count() !== table_carTax.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_carTax.rows({
            selected: true,
            page: 'current'
        }).count() !== table_carTax.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on('click', 'button[name="btn-claim"]', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return;
        }

        var row = table_carTax.row($(this).closest('tr'));
        var data = row.data();
        var response = claimCarTax(data.id);
        if (response.status === 'success') {
            if (!isEmpty(response.data)) {
                row.data(response.data).invalidate();
                table_carTax.draw();
            }
        }
    });

    $('button[name="btn-claim"]').on('click', function(e) {

        var selectedRows = table_carTax.rows({
            selected: true,
            page: 'current'
        });
        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return;
        }
        var rowDatas = selectedRows.data();
        var isValid = isValidSelection(rowDatas);
        if (!isValid) {
            alert($.i18n.prop('page.accounts.claim.cartax.validation.claimstatus.notsame'))
            return e.preventDefault();
        }
        table_carTax.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var node = $(this.node());
            let row = table_carTax.row($(node).closest('tr'))
            let data = row.data();
            var response = claimCarTax(data.id);
            if (response.status === 'success') {
                if (!isEmpty(response.data)) {
                    // row.data(response.data).invalidate();
                    table_carTax.ajax.reload();
                }
            }
        })
    })

    //on open carTax modal
    var modalCarTaxClaimTriggerEle;
    var element;
    var modalCarTaxClaim = $('#modal-carTax-claim');
    modalCarTaxClaim.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var selectedRows = table_carTax.rows({
            selected: true,
            page: 'current'
        });
        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();

        }

        var rowDatas = selectedRows.data();
        var isValid = isClaimSelection(rowDatas);
        if (!isValid) {
            alert("Please Select the stock with claim status")
            return event.preventDefault();
        }
        var d = new Date();
        var cuurentDate = moment(d, "DD-MM-YYYY").format('DD-MM-YYYY');
        $('#modal-carTax-claim').find('input#received-date').datepicker("setDate", cuurentDate);
        $('#modal-carTax-claim').find('div.vehicle span.no-of-vehicles').html(selectedRows.count());
        $(modalCarTaxClaim).find('select[name="remitAuction"]').empty()
        $.getJSON(myContextPath + "/data/cartaxclaimed", function(data) {
            carTaxApproveJson = data;
            $('#remitAuction').select2({
                allowClear: true,
                width: '100%',
                placeholder: 'Select Remit Auction',
                data: $.map(carTaxApproveJson, function(item) {
                    return {
                        id: item.daybookId,
                        text: item.carTaxApprove,
                        data: item
                    };
                })
            }).val('').trigger("change");
        });

        table_carTax.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table_carTax.row(this).data();
            $('#append-row').removeClass('hidden')
            element = $('#clone-row-hide').clone();
            $(element).removeClass('hidden')
            $(element).appendTo('#append-row');
            element.find('input[name="id"]').val(data.id);
            element.find('input[name="chassisNo"]').val(data.chassisNo);
            setAutonumericValue(element.find('input[name="actualAmount"]'), data.roadTax);
            setAutonumericValue(element.find('input[name="amount"]'), data.roadTax);
        });
        updateFooter();

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $(this).find('select').val('').trigger('change');
        $("#append-row").find('div.row').remove();
    }).on('change', '#remitAuction', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data)) {
            $('#modal-carTax-claim').find('input#remit-date').datepicker("setDate", data.data.remitDate);
            setAutonumericValue($('#totalAmount'), data.data.amount);

        } else {
            $('#modal-carTax-claim').find('input#remit-date').datepicker("setDate", "");
            $('#totalAmount').val("");
        }
    }).on("keyup", "input.autonumber", function(setting) {
        //update table footer`
        updateFooter();
    }).on('click', '#save-carTax-modal', function() {
        if (!$('#modal-carTax-claim form#update-car-tax-form').find('select').valid()) {
            return;
        }
        var totalReceivedAmount = $('#modal-carTax-claim').find('div.summary-container span.total-received').autoNumeric('init').autoNumeric('get');
        var totalAmount = $('#modal-carTax-claim').find('#totalAmount').autoNumeric('init').autoNumeric('get');
        if (!(totalReceivedAmount == totalAmount)) {
            alert("Total Received amount and total amount must be same")
            return false;
        }
        let data_array = [];
        var dayBookId = modalCarTaxClaim.find("select#remitAuction option:selected").val()
        var receivedDate = modalCarTaxClaim.find('input#received-date').val();
        var elements = modalCarTaxClaim.find('#append-row').find('div.row')
        $(elements).each(function(i) {
            let data = {};
            let loopingEle =$(elements[i]);
            data["id"] = $(loopingEle).find('input[name="id"]').val();
            data["chassisNo"] = $(loopingEle).find('input[name="chassisNo"]').val();
            data["amount"] = $(loopingEle).find('input[name="amount"]').autoNumeric('get');
            data_array.push(data);
        })
        var response = updateClaimReceivedAmount(data_array, $.param({
            "dayBookId": dayBookId,
            "receivedDate": receivedDate
        }))
        if (response.status === 'success') {

            // row.data(response.data).invalidate();
            table_carTax.ajax.reload();
            modalCarTaxClaim.modal('toggle');

        }

    })

    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();

    //Receivable Received draw dataTable
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        var status = $('input[name="radioReceivedFilter"]:checked').val();
        if (!isEmpty(status) && status == 2) {
            if (aData[8].length == 0 || aData[8] == 0 || aData[8] == 1) {
                return false;
            }
        } else if (!isEmpty(status) && status != 2) {
            if (aData[8].length == 0 || aData[8] == 2) {
                return false;
            }
        }
        if (typeof carTax_min != 'undefined' && carTax_min.length != '') {
            if (aData[3].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[3], 'DD-MM-YYYY')._d.getTime();
            }
            if (carTax_min && !isNaN(carTax_min)) {
                if (aData._date < carTax_min) {
                    return false;
                }
            }
            if (carTax_max && !isNaN(carTax_max)) {
                if (aData._date > carTax_max) {
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

        return true;
    });

});

// update claim received amount
function updateClaimReceivedAmount(data, params) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/accounts/claim/carTax/received?" + params,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

//claim CarTax amount
function claimCarTax(id) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        url: myContextPath + "/accounts/claim/carTax?id=" + id,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function isValidSelection(data) {
    //var tmpPData = ''
    var tmpClaimStatus = '';
    for (var i = 0; i < data.length; i++) {
        var tableData = data[i];
        if (i == 0) {
            // tmpPData = tableData.purchaseDate
            tmpClaimStatus = tableData.carTaxClaimStatus;

        }
        //tmpPData == tableData.purchaseDate -> removed purchase date equal
        if (!(tmpClaimStatus == tableData.carTaxClaimStatus)) {
            return false;
        }
    }
    return true;
}
function isClaimSelection(data) {
    //var tmpPData = ''
    var tmpClaimStatus = '';
    for (var i = 0; i < data.length; i++) {
        var tableData = data[i];
        //         if (i == 0) {
        //             // tmpPData = tableData.purchaseDate
        //             tmpClaimStatus = tableData.carTaxClaimStatus;

        //         }
        //tmpPData == tableData.purchaseDate -> removed purchase date equal
        if (!(tableData.carTaxClaimStatus == 1)) {
            return false;
        }
    }
    return true;
}

function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);
    $('#count-carTax').html(data.carTax);
    $('#count-insurance').html(data.insurance);
    $('#count-radiation').html(data.radiation);

}

// function updateFooter() {
//     var totalAcutualAmount = 0;
//     var totalReceivedAmount = 0;
//     $('#modal-carTax-claim').find('input[name="amount"]').each(function() {
//         totalReceivedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
//     })
//     $('#modal-carTax-claim').find('div.summary-container span.total-received').autoNumeric('init').autoNumeric('set', totalReceivedAmount);
//     $('#modal-carTax-claim').find('input[name="actualAmount"]').each(function() {
//         totalAcutualAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
//     })
//     $('#modal-carTax-claim').find('div.summary-container span.total-actual').autoNumeric('init').autoNumeric('set', totalAcutualAmount);

// }

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // purchase cost total
    var roadTax = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="roadTax"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    // commission amount tax total
    var carTaxClaimReceivedAmount = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="carTaxClaimReceivedAmount"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
    $('#table-claim-carTax>tfoot>tr.sum').find('span.totalAmount').autoNumeric('init').autoNumeric('set', roadTax);

    $('#table-claim-carTax>tfoot>tr.sum').find('span.carTaxTotal').autoNumeric('init').autoNumeric('set', carTaxClaimReceivedAmount);

}

