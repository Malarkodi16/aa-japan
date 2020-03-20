var supplierJson, transportersJson, locationJson, countryJson, screenNameFlag, stockUrl;
$(function() {
    screenNameFlag = $('input[name="screenNameFlag"]').val();
    $.getJSON(myContextPath + "/data/accname.json", function(data) {
        // var coacategorytypeJson = data;
        $('#add-new-category select#accountType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.subAccount + '(' + item.account + ')' + '(' + item.code + ')',
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/data/active/suppliers.json", function(data) {
        $('#modal-add-payment select[name="purchasedSupplier"]').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).val('').trigger("change")

    });
    $('#modal-add-payment select#purchasedAuctionHouse,select#invoiceItemType').select2({
        allowClear: true,
    });
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {

        $(this).find('input,select,textarea').valid();
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        $('#modal-arrange-transport').find('select[name="destinationCountry"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(countryJson, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        })
        $('#item-vechicle-clone').find('select[name="destinationCountry"]').select2('destroy')
    })

    //set status
    setShippingDashboardStatus();
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        });
        var newOption = new Option("Others","others",false,false);
        // Append it to the select
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').append(newOption);
        $('#item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').select2('destroy')
    });
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {

        $('#transport-items').find('select[name="forwarder"]').select2({
            placeholder: "Select forwarder address",
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
        $('#item-vechicle-clone').find('select[name="forwarder"]').select2('destroy')
    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#modal-arrange-transport').find('.transporter').select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
        $('#item-vechicle-clone').find('select[name="transporter"]').select2('destroy');
    })

    $('#cancelledStock').select2({

        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {

            url: myContextPath + "/stock/cancelled/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    invoiceDate: $('#modal-add-payment input[name="invoiceDate"]').val(),
                    // auctionCompany: $('#modal-add-payment
                    // select[name="purchasedSupplier"]').val(),
                    // auctionHouse: $('#modal-add-payment
                    // select[name="purchasedAuctionHouse"]').val(),
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    $('#stockVisible').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });
    $('#reauctionStock').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {

            url: myContextPath + "/stock/reauction/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    invoiceDate: $('#modal-add-payment input[name="invoiceDate"]').val(),
                    // auctionCompany: $('#modal-add-payment
                    // select[name="purchasedSupplier"]').val(),
                    // auctionHouse: $('#modal-add-payment
                    // select[name="purchasedAuctionHouse"]').val(),
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });
    $('#transportStock').select2({

        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {

            url: myContextPath + "/stock/rikuso/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    // auctionCompany: $('#modal-add-payment
                    // select[name="purchasedSupplier"]').val(),
                    // auctionHouse: $('#modal-add-payment
                    // select[name="purchasedAuctionHouse"]').val(),
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });
    $('#penaltyStock').select2({

        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {

            url: myContextPath + "/stock/cancelled/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    invoiceDate: $('#modal-add-payment input[name="invoiceDate"]').val(),
                    // auctionCompany: $('#modal-add-payment
                    // select[name="purchasedSupplier"]').val(),
                    // auctionHouse: $('#modal-add-payment
                    // select[name="purchasedAuctionHouse"]').val(),
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    $('#recycleCarTaxPaidStock').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    let takeOutStockEle = $('#takeOutStock');
    $(takeOutStockEle).select2({
        allowClear: true,
        minimumInputLength: 2,
        closeOnSelect: false,
        placeholder: "Search by Stock No. or Chassis No.",
        width: "100%",
        ajax: {

            url: myContextPath + "/stock/take-out/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    invoiceDate: $('#modal-add-payment input[name="invoiceDate"]').val(),
                    auctionCompany: $('#modal-add-payment select[name="purchasedSupplier"]').val(),
                    auctionHouse: $('#modal-add-payment select[name="purchasedAuctionHouse"]').val(),
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                let newOption;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });

                }

                return {
                    results: $.each(results, function(key, value) {
                        $(takeOutStockEle).append($("<option></option>").attr("value", value.id).text(value.text));
                    })
                }

            },
            cache: true
        }
    })
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })

    // autoNumeric
    $('.charge').autoNumeric('init');
    // Select 2
    $('#table-filter-auctionhouse').select2({
        placeholder: "All",
        allowClear: true
    })
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    function canConfirmBtnEnable() {
        if ($('#input-invoice-no').val().length == 0 || $('#input-invoice-due-date').val().length == 0 || table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            return false;
        }

        return true;
    }
    // Datatable
    var checkIfAccounts = (screenNameFlag == 'accounts') ? false : true;
    console.log(checkIfAccounts);
    var table = $('#table-purchased').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        //         dom: "<'row'<'col-sm-12'tr>><'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ordering": true,
        "order": [],
        "pageLength": 25,
        "pagingType": "full_numbers",
        "ajax": {
            url: myContextPath + "/stock/purchased-data",
            data: function(data) {
                data.screen = ifNotValid(screenNameFlag, '')
            }
        },

        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            "data": "id",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input type="hidden" name="invoiceNo" value="' + ifNotValid(row.code, '') + '"/><input class="selectBox" id="check-box-select" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">' + '<input type="hidden" name="stockNo" value="' + row.stockNo + '"/>' + '<input type="hidden" name="invoiceType" value="' + ifNotValid(row.purchaseInfoType, '') + '"/>' + '<input type="hidden" name="invoiceName" value="' + row.supplierCode + '"/>' + '<input type="hidden" name="supplierCode" value="' + row.supplierCode + '"/>' + '<input type="hidden" name="supplierName" value="' + row.supplierName + '"/>' + '<input type="hidden" name="model" value="' + row.model + '"/>' + '<input type="hidden" name="maker" value="' + row.maker + '"/>';

                }
                return data;
            }
        }, {
            targets: 1,
            // "orderable": false,
            "data": "purchaseDate",
            "width": "75px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return data;
            }
        }, {
            targets: 2,
            //"orderable": false,
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
            // "orderable": false,
            "data": "model",
            "visible": checkIfAccounts
        }, {
            targets: 4,
            "orderable": true,
            "data": "auctionInfoLotNo"
        }, {
            targets: 5,
            "orderable": true,
            "data": "auctionInfoPosNo",
            "visible": false

        }, {
            targets: 6,
            //  "orderable": false,
            "data": "supplierName"
        }, {
            targets: 7,
            "orderable": true,
            "data": "auctionHouse"
        }, {
            targets: 8,
            // "orderable": false,

            "data": "type"
        }, {
            targets: 9,
            //"orderable": false,
            "data": "purchaseCost",
            "width": "100px",
            "render": function(data, type, row) {
                var pCost = Number(row.purchaseCost);
                var pTax = Number(row.purchaseCostTax);
                var pTot = (pCost * pTax) / 100;
                var pTotTax = pCost + pTot;
                data = data == null ? '' : data;
                if (type === 'display') {
                    var readonly = (ifNotValid(row.purchaseCostFlag, '') == 1) ? 'readonly' : '';
                    return '<div class="form-inline">' + '<input type="text" name="purchaseCost"  class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0" style="width: 150px" ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '></br>' + '<input type="hidden" name="purchaseCostTax" value="' + ifNotValid(row.purchaseCostTax, 0) + '" class="form-control autonumber mt-5"  data-v-max="100" data-v-min="0" data-m-dec="0" data-a-sign=" %" data-p-sign="s" style="width:150px">' + '<input type="text" name="taxValue" value="' + ifNotValid(pTax, 0) + '" class="form-control autonumber mr-5 mt-5" data-a-sign=" %" data-p-sign="s" data-m-dec="0"  style="width:55px">' + '<input type="hidden" name="hiddenPurchaseTaxAmt" value="' + ifNotValid(pTot, 0) + '" class="form-control autonumber mt-5" data-m-dec="0" data-a-sign="¥ " data-p-sign="s" style="width:150px">' + '<input type="text" name="purchaseCostTaxAmount" value="' + ifNotValid(row.purchaseCostTaxAmount, pTot) + '" class="form-control autonumber ml-5 mt-5" data-a-sign="¥ " data-m-dec="0"  style="width:85px"  ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '>' + '</div>'

                }
                return data;
            }
        }, {
            targets: 10,
            //   "orderable": false,
            "data": "commision",
            "width": "100px",
            "render": function(data, type, row) {
                var cCost = Number(row.commision);
                var cTax = Number(row.commisionTax);
                var cTot = (cCost * cTax) / 100;
                var cTotTax = cCost + cTot;
                data = data == null ? '' : data;
                if (type === 'display') {
                    var readonly = (ifNotValid(row.commissionFlag, '') == 1) ? 'readonly' : '';
                    return '<div class="form-inline">' + '<input name="commision" type="text" class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0" style=" width: 150px;" ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '></br>' + '<input type="hidden" name="commisionTax" value="' + ifNotValid(row.commisionTax, 0) + '" class="form-control autonumber mt-5" data-v-max="100" data-v-min="0" data-a-sign=" %" data-m-dec="0" data-p-sign="s" style="width: 150px" />' + '<input type="text" name="commisionTaxValue" value="' + ifNotValid(cTax, 0) + '" class="form-control autonumber mr-5 mt-5" data-a-sign=" %" data-p-sign="s" data-m-dec="0"  style="width:55px">' + '<input type="hidden" name="hiddenCommisionTaxAmt" value="' + ifNotValid(cTot, 0) + '" class="form-control autonumber mt-5" data-m-dec="0" data-a-sign="¥ " data-p-sign="s" style="width:150px">' + '<input type="text" name="commisionTaxAmount" value="' + ifNotValid(row.commisionTaxAmount, cTot) + '" class="form-control autonumber ml-5 mt-5" data-a-sign="¥ " data-m-dec="0"  style="width:85px" ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '>'
                    '</div>' + '<span class="help-block"></span>';
                }
                return data;
            }
        }, {
            targets: 11,
            //  "orderable": false,
            "data": "roadTax",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var readonly = (ifNotValid(row.numberPlate, '') == 'no') ? 'readonly' : (ifNotValid(row.roadTaxFlag, '') == 1) ? 'readonly' : '';
                    return '<input name="roadTax" type="text" class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0" style=" width: 80px; " ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '/>';
                }
                return data;
            }
        }, {
            targets: 12,
            // "orderable": false,
            "data": "recycle",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    // '+ifNotValid(row.sRecycle,'')=='yes'?'readonly':''+'
                    var readonly = (ifNotValid(row.recycleFlag, '') == 1) ? 'readonly' : '';
                    return '<input name="recycle" type="text"  class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0" style=" width: 80px;" ' + (row.type != 'PURCHASE' ? 'readonly' : readonly) + '/>';
                }
                return data;
            }
        }, {
            targets: 13,
            //  "orderable": false,
            "data": "otherCharges",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    let value = ifNotValid(data, 0);
                    if (row.type == 'CASH BACK') {
                        value *= -1;
                    }
                    var oCost = Number(value);
                    var oTax = Number(ifNotValid(row.otherChargesTax, 0));
                    var oTot = oCost * (oTax / 100);
                    var oTotTax = oCost + oTot;
                    // return '<input name="otherCharges" type="text"
                    // class="form-control autonumber" value="' + value + '"
                    // data-a-sign="&yen; " data-m-dec="0" style=" width: 100px;
                    // " />';
                    return '<div class="form-inline">' + '<input type="text" name="otherCharges"  class="form-control autonumber" value="' + value + '" data-a-sign="&yen; " data-m-dec="0" style="width: 150px"></br>' + '<input type="hidden" name="otherChargesTax" value="' + ifNotValid(row.otherChargesTax, 0) + '" class="form-control autonumber mt-5"  data-v-max="100" data-v-min="0" data-m-dec="0" data-a-sign=" %" data-p-sign="s" style="width:150px">' + '<input type="text" name="othersTaxValue" value="' + ifNotValid(oTax, 0) + '" class="form-control autonumber mr-5 mt-5" data-a-sign=" %" data-p-sign="s" data-m-dec="0"  style="width:55px">' + '<input type="hidden" name="hiddenOthersTaxAmt" value="' + ifNotValid(row.othersCostTaxAmount, oTot) + '" class="form-control autonumber mt-5" data-m-dec="0" data-a-sign="¥ " data-p-sign="s" style="width:150px">' + '<input type="text" name="othersCostTaxAmount" value="' + ifNotValid(row.othersCostTaxAmount, oTot) + '" class="form-control autonumber ml-5 mt-5" data-a-sign="¥ " data-m-dec="0"  style="width:85px">' + '</div>'
                }
                return data;
            }
        }, {
            targets: 14,
            //  "orderable": false,

            "render": function(data, type, row) {
                // purchase price
                var purchasePrice = Number(ifNotValid(row.purchaseCost, 0));
                var purchaseCostTaxPercent = Number(ifNotValid(row.purchaseCostTax, 0));
                var purchaseCostTax = (purchasePrice * purchaseCostTaxPercent) / 100;
                var purchaseTaxAmount = Number(ifNotValid(row.purchaseCostTaxAmount, purchaseCostTax));
                // recycle price
                var recycleAmount = Number(ifNotValid(row.recycle, 0));
                // commission price
                var commisionAmount = Number(ifNotValid(row.commision, 0));
                var commisionTaxPercent = Number(ifNotValid(row.commisionTax, 0));
                var commisionTax = (commisionAmount * commisionTaxPercent) / 100;
                var commisionTaxAmount = Number(ifNotValid(row.commisionTaxAmount, commisionTax));
                // Other Charges
                var otherCharges = Number(ifNotValid(row.otherCharges, 0));
                var otherChargesTaxPercent = Number(ifNotValid(row.otherChargesTax, 0));
                var otherChargesTaxAmt = (otherCharges * otherChargesTaxPercent) / 100;
                var otherChargesTaxAmount = Number(ifNotValid(row.otherChargesTaxAmount, otherChargesTaxAmt));

                var roadTaxAmount = Number(ifNotValid(row.roadTax, 0));
                var total = purchasePrice + recycleAmount + commisionAmount + otherCharges + roadTaxAmount;
                var totalTax = purchaseTaxAmount + commisionTaxAmount + otherChargesTaxAmount;
                var totalTaxIncluded = total + totalTax;
                if (row.type == 'CASH BACK') {
                    totalTaxIncluded *= -1;
                }

                if (type === 'display') {
                    return '<input name="subtotal" type="text"  class="form-control autonumber" value="' + totalTaxIncluded + '" data-a-sign="&yen; " data-m-dec="0" style=" width: 100px; " disabled/>';
                }
                return data;
            }
        }, {
            targets: 15,
            //  "orderable": false,
            "data": "purchaseInfoType",
            "visible": false // "searchable": false
        }, {
            targets: 16,
            //   "orderable": false,
            "data": "supplierCode",
            "visible": false // "searchable": false
        }, {
            targets: 17,
            //   "orderable": false,
            "data": "auctionHouseId",
            "visible": false // "searchable": false
        }, {
            targets: 18,
            //     "orderable": false,
            "data": "transportationStatus",
            "visible": checkIfAccounts,
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == "0") {
                    status = "Idle"
                    className = "default"
                } else if (data == "1") {
                    status = "In Transit"
                    className = "info"

                } else if (data == "2") {
                    status = "Completed"
                    className = "success"

                }
                let html = '<span class="label label-' + ifNotValid(className, 'default') + '" style="min-width:100px">' + ifNotValid(status, 'Idle') + '</span>';
                return (row.type == 'PURCHASE' ? html : '');
            }
        }, {
            targets: 19,
            "orderable": false,
            "width": '100px',
            "data": "stockNo",
            "render": function(data, type, row) {
                var html = "";
                if (screenNameFlag == "accounts") {
                    html += '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Add Payment" id="add-payment" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-payment"><i class="fa fa-plus"></i></a>';
                    if (row.type != 'PURCHASE') {
                        html += '<a type="button" class="btn btn-danger ml-5 btn-xs" title="Delete Item" id="delete-invoice-item"><i class="fa fa-fw  fa-trash-o"></i></a>';
                    } else {
                        html += '<a type="button" class="btn btn-default ml-5 btn-xs" title="Edit Purchase" id="accounts-edit-purchase"><i class="fa fa-fw fa-edit"></i></a>'
                    }
                } else {
                    html = '<a type="button" class="btn btn-danger ml-5 btn-xs" title="Cancel Purchase" id="cancel-purchase"><i class="fa fa-fw fa-close"></i></a>'
                    html += '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Edit Purchase" id="edit-purchase"><i class="fa fa-fw fa-edit"></i></a>'

                }
                return '<div style="width:100px;">' + html + '</div>';
            }

        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init')

        },
        "rowCallback": function(row, data) {
            if (data.type == 'PURCHASE') {
                $(row).addClass('can-transaport-arrange');
            }

        }

    });

    function updateFooter(table) {

        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        // page total
        // purchase cost total
        var pagePurchaseCostTotal = table.column(9, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('input[name="purchaseCost"]').autoNumeric('init').autoNumeric('get')));
            var tax = Number(isValid($(b).find('input[name="purchaseCostTax"]').autoNumeric('init').autoNumeric('get')));
            var taxAmount = Number((amount * tax) / 100);
            var total = Number((amount));

            return intVal(a) + total;
        }, 0);
        // purchase cost tax total
        var pagePurchaseCostTaxTotal = table.column(9, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('input[name="purchaseCost"]').autoNumeric('init').autoNumeric('get')));
            var tax = Number(isValid($(b).find('input[name="purchaseCostTax"]').autoNumeric('init').autoNumeric('get')));
            var purchaseTax = Number(isValid($(b).find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('get')));
            var taxAmount = Number((amount * tax) / 100);
            var purchaseCostTaxAmount = Number(ifNotValid(purchaseTax, taxAmount));

            return intVal(a) + purchaseCostTaxAmount;
        }, 0);
        // commission amount total
        var pageCommissionTotal = table.column(10, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="commision"]').autoNumeric('init').autoNumeric('get')));
            var tax = Number(isValid($(b).find('input[name="commisionTax"]').autoNumeric('init').autoNumeric('get')));
            // var taxAmount = Number((amount * tax) / 100);
            var total = Number(amount);

            return intVal(a) + total;
        }, 0);
        // commission amount tax total
        var pageCommissionTaxTotal = table.column(10, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="commision"]').autoNumeric('init').autoNumeric('get')));
            var tax = Number(isValid($(b).find('input[name="commisionTax"]').autoNumeric('init').autoNumeric('get')));
            var commisionTax = Number(isValid($(b).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('get')));
            var taxAmount = Number((amount * tax) / 100);
            var commisionTaxAmount = Number(ifNotValid(commisionTax, taxAmount));

            return intVal(a) + commisionTaxAmount;
        }, 0);
        // road tax total
        var pageRoadTaxTotal = table.column(11, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="roadTax"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);
        // recycle amount total
        var pageRecycleTotal = table.column(12, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="recycle"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);

        // others amount total
        var pageOtherChargesTotal = table.column(13, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);
        // others tax total
        var pageOtherChargesTaxTotal = table.column(13, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('get')));
            var tax = Number(isValid($(b).find('input[name="othersTaxValue"]').autoNumeric('init').autoNumeric('get')));
            var othersTax = Number(isValid($(b).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('get')));
            var taxAmount = Number((amount * tax) / 100);
            var othersTaxAmount = Number(ifNotValid(othersTax, taxAmount));

            return intVal(a) + othersTaxAmount;
        }, 0);

        // $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');

        $('#table-purchased>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.purchaseTaxTotal').autoNumeric('init').autoNumeric('set', pagePurchaseCostTaxTotal);

        $('#table-purchased>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.commisionTaxTotal').autoNumeric('init').autoNumeric('set', pageCommissionTaxTotal);

        $('#table-purchased>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.otherChargesTaxTotal').autoNumeric('init').autoNumeric('set', pageOtherChargesTaxTotal);

        $('#table-purchased>tfoot>tr.sum').find('span.autonumber.pagetotal.purchaseTotal').autoNumeric('init').autoNumeric('set', pagePurchaseCostTotal);

        $('#table-purchased>tfoot>tr.sum').find('span.autonumber.pagetotal.commisionTotal').autoNumeric('init').autoNumeric('set', pageCommissionTotal);

        $('#table-purchased>tfoot>tr.sum').find('span.autonumber.pagetotal.roadTaxTotal').autoNumeric('init').autoNumeric('set', pageRoadTaxTotal);

        $('#table-purchased>tfoot>tr.sum').find('span.autonumber.pagetotal.recycleTotal').autoNumeric('init').autoNumeric('set', pageRecycleTotal);

        $('#table-purchased>tfoot>tr.sum').find('span.autonumber.pagetotal.othersTotal').autoNumeric('init').autoNumeric('set', pageOtherChargesTotal);

        var pageOverallTotal = Number(pagePurchaseCostTotal) + Number(pagePurchaseCostTaxTotal) + Number(pageCommissionTotal) + Number(pageCommissionTaxTotal) + Number(pageRoadTaxTotal) + Number(pageRecycleTotal) + Number(pageOtherChargesTotal) + Number(pageOtherChargesTaxTotal);

        $('#table-purchased tfoot').find('tr#grandTotal').find('.pagetotal').autoNumeric('init').autoNumeric('set', pageOverallTotal);

    }
    table.on("keyup", 'td input[name="purchaseCost"], td input[name="taxValue"]', function(setting) {
        let td = $(this).closest('td');
        var pCost = Number(td.find('input[name="purchaseCost"]').autoNumeric('init').autoNumeric('get'));
        var pTax = td.find('input[name="taxValue"]').autoNumeric('init').autoNumeric('get');
        var pTot = (pCost * pTax) / 100;
        var pTotTax = pCost + pTot;
        $(this).closest('td').find('input[name="purchaseCost"]').autoNumeric('init').autoNumeric('set', pCost)
        $(this).closest('td').find('input[name="purchaseCostTax"]').autoNumeric('init').autoNumeric('set', pTax)
        $(this).closest('td').find('input[name="hiddenPurchaseTaxAmt"]').autoNumeric('init').autoNumeric('set', pTot)
        $(this).closest('td').find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set', pTot)
    })
    // table.on("keyup", 'td input[name="purchaseCostTaxAmount"]',
    // function(e,setting) {
    // let td = $(this).closest('tr');
    // let previousValue = table.row(td).data();
    // console.log(previousValue.purchaseCostTaxAmount);
    // var pTot =
    // td.find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('get');
    // $(this).closest('td').find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set',
    // pTot)
    // })
    table.on("keyup", 'td input[name="commision"], td input[name="commisionTaxValue"]', function(setting) {
        let td = $(this).closest('td');
        console.log(td);
        var cCost = Number(td.find('input[name="commision"]').autoNumeric('init').autoNumeric('get'));
        var cTax = td.find('input[name="commisionTaxValue"]').autoNumeric('init').autoNumeric('get');
        var cTot = (cCost * cTax) / 100;
        var cTotTax = cCost + cTot;
        $(this).closest('td').find('input[name="commision"]').autoNumeric('init').autoNumeric('set', cCost)
        $(this).closest('td').find('input[name="commisionTax"]').autoNumeric('init').autoNumeric('set', cTax)
        $(this).closest('td').find('input[name="hiddenCommisionTaxAmt"]').autoNumeric('init').autoNumeric('set', cTot)
        $(this).closest('td').find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('set', cTot)
    })
    table.on("keyup", 'td input[name="otherCharges"], td input[name="othersTaxValue"]', function(setting) {
        let td = $(this).closest('td');
        var oCost = Number(td.find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('get'));
        var oTax = td.find('input[name="othersTaxValue"]').autoNumeric('init').autoNumeric('get');
        var oTot = (oCost * oTax) / 100;
        var oTotTax = oCost + oTot;
        $(this).closest('td').find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('set', oCost)
        $(this).closest('td').find('input[name="otherChargesTax"]').autoNumeric('init').autoNumeric('set', oTax)
        $(this).closest('td').find('input[name="hiddenOthersTaxAmt"]').autoNumeric('init').autoNumeric('set', oTot)
        $(this).closest('td').find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('set', oTot)
    })

    table.on("keyup", "input.autonumber", function(setting) {
        var container = $(this).closest('tr');
        // update subtotal
        container.find('input[name="subtotal"]').autoNumeric('init').autoNumeric('set', subtotal(container));
        // update table footer`
        updateFooter(table);
        // auto select checkbox
        var purchasedCost = $(this).closest('tr').find('input[name="purchaseCost"]').autoNumeric('init').autoNumeric('get');
        var commision = $(this).closest('tr').find('input[name="commision"]').autoNumeric('init').autoNumeric('get');
        var recycle = $(this).closest('tr').find('input[name="recycle"]').autoNumeric('init').autoNumeric('get');
        if (!isEmpty(purchasedCost) && !isEmpty(commision) && !isEmpty(recycle)) {
            var row = $(this).closest('tr');
            row.find('#check-box-select').prop('checked', true);
            var tr = table.row(row).select();
        } else {
            var row = $(this).closest('tr');
            row.find('#check-box-select').prop('checked', false);
            var tr = table.row(row).deselect();

        }

    })
    var subtotal = function(element) {
        var purchaseCostEle = element.find('input[name="purchaseCost"]');
        var recycleAmountEle = element.find('input[name="recycle"]');
        var commisionAmountEle = element.find('input[name="commision"]');
        var otherChargesEle = element.find('input[name="otherCharges"]');
        var roadTaxEle = element.find('input[name="roadTax"]');
        var taxEle = element.find('input[name="taxValue"]');
        var commisionTaxEle = element.find('input[name="commisionTaxValue"]');
        var otherChargesTaxEle = element.find('input[name="othersTaxValue"]');
        var purchaseCostTaxAmount = element.find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('get');
        var commisionTaxAmount = element.find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('get');
        var otherChargesTaxAmount = element.find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('get');
        // purchase price
        var purchasePrice = Number(purchaseCostEle.autoNumeric('init').autoNumeric('get'));
        var purchaseCostTaxPercent = Number(taxEle.autoNumeric('init').autoNumeric('get'));
        var purchaseCostTax = (purchasePrice * purchaseCostTaxPercent) / 100;
        var purchaseTaxAmount = Number(ifNotValid(purchaseCostTaxAmount, purchaseCostTax));
        // recycle price
        var recycleAmount = Number(recycleAmountEle.autoNumeric('init').autoNumeric('get'));
        // commission price
        var commisionAmount = Number(commisionAmountEle.autoNumeric('init').autoNumeric('get'));
        var commisionTaxPercent = Number(commisionTaxEle.autoNumeric('init').autoNumeric('get'));
        var commisionTax = (commisionAmount * commisionTaxPercent) / 100;
        var commisionTaxAmount = Number(ifNotValid(commisionTaxAmount, commisionTax));
        // Other Charges
        var otherCharges = Number(otherChargesEle.autoNumeric('init').autoNumeric('get'));
        var othersCostTaxPercent = Number(otherChargesTaxEle.autoNumeric('init').autoNumeric('get'));
        var othersCostTax = (otherCharges * othersCostTaxPercent) / 100;
        var othersTaxAmount = Number(ifNotValid(otherChargesTaxAmount, othersCostTax));

        var roadTaxAmount = Number(roadTaxEle.autoNumeric('init').autoNumeric('get'));
        var total = purchasePrice + recycleAmount + commisionAmount + otherCharges + roadTaxAmount;
        var totalTax = purchaseTaxAmount + commisionTaxAmount + othersTaxAmount;
        var totalTaxIncluded = total + totalTax;
        // set total amount

        return totalTaxIncluded;

    }

    $('#item-vehicle-container').slimScroll({
        start: 'bottom',
        height: '',
        railVisible: true,
    });
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
    }).on("select", function(e, index, c, indexs, d) {
        // check if purchased date and supplier are same
        // $.param
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
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            addPurchasedListValidations($(this.node()));
        })

    }).on("deselect", function() {
        // set supplier default due date
        // setDueDate();
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
        table.rows({
            selected: false
        }).every(function(rowIdx, tableLoop, rowLoop) {
            removePurchasedListValidations($(this.node()));
        })

    });

    function isValidSelection(data) {
        // var tmpPData = ''
        var tmpPSupplier = '';
        for (var i = 0; i < data.length; i++) {
            var tableData = data[i];
            if (i == 0) {
                // tmpPData = tableData.purchaseDate
                tmpPSupplier = tableData.supplierCode;
            }
            // tmpPData == tableData.purchaseDate -> removed purchase date equal
            if (!(tmpPSupplier == tableData.supplierCode)) {
                return false;
            }
        }
        return true;
    }
    function getInvoiceDueDate(supplierCode, date) {
        var result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'get',
            async: false,
            url: myContextPath + '/a/supplier/info/' + supplierCode + '.json',
            success: function(data) {
                result = data;
            }
        })
        return result;
    }

    // Add new category
    var addPaymentEle = $('#add-new-category')
    $(addPaymentEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

    }).on('hide.bs.modal', function(e) {

        $(this).find('input,textarea').val('');
        $(this).find('select').val('').trigger('change');
        // account();
    }).on('click', '#btn-create-category', function() {
        if (!$('#add-new-category-form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        let isChecked = addPaymentEle.find('input#stockView').is(":checked");
        let claimed = addPaymentEle.find('input#claimed').is(":checked");
        var data = {};
        data['type'] = $('#categoryDesc').val();
        data['coaCode'] = $('#accountType').val();
        if (isChecked) {
            data['stockView'] = 1;
        } else {
            data['stockView'] = 0;
        }

        if (claimed) {
            data['claimed'] = 1;
        } else {
            data['claimed'] = 0;
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/create/auction/payment/category",
            contentType: "application/json",
            async: true,
            success: function(data) {

                $('#add-new-category').modal('toggle');

                // $.getJSON(myContextPath + "/data/paymentCategory.json",
                // function(data) {
                // // var cloneRowCategory =
                // $('#payment-item-table').find('tr.clone-row').find('.category');
                // // var CategoryFirstRow =
                // $('#payment-item-table').find('tr.first-row').find('.category');
                // // var StockFirstRow =
                // $('#payment-item-table').find('tr.first-row').find('.stockNo');
                // $('#modal-add-payment .category').select2({
                // allowClear: true,
                // width: "150px",
                // data: $.map(data, function(item) {
                // return {
                // id: item.categoryCode,
                // text: item.category
                // };
                // })
                // }).val('').trigger("change");

                // });
            }
        });
    });

    $('#modal-confirm').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var selectedRows = table.rows({
            selected: true,
            page: 'all'
        });

        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        var rowDatas = selectedRows.data();

        var isValid = isValidSelection(rowDatas);
        if (!isValid) {
            alert($.i18n.prop('page.purchased.invoice.confirm.validation.singleinvoice'))
            return e.preventDefault();
        }
        if (!$('#form-purchased').valid() || !isPurchaseEntryValid($(selectedRows.nodes()))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return e.preventDefault();
        }

        // set supplier default due date
        var supplierCode = rowDatas[0].supplierCode;
        // var supplierName = rowDatas[0].supplierName;
        var purchaseDate = rowDatas[0].purchaseDate;
        // var sPurchaseDate = rowDatas[0].purchaseDate;
        var response = getInvoiceDueDate(supplierCode);
        var maxDueDays = response.data.maxDueDays;
        var dueDate = moment(purchaseDate, "DD-MM-YYYY").add(maxDueDays, 'days').format('DD-MM-YYYY');
        $(this).find('#input-invoice-due-date').datepicker("setDate", dueDate);
        var subTotalAmount = 0;
        selectedRows.nodes().each(function(index, value) {
            var subTotalValue = Number($(index).find('input[name="subtotal"]').autoNumeric('init').autoNumeric('get'));
            subTotalAmount += subTotalValue
        })
        // console.log(subTotalAmount);
        $(this).find('input[name="subTotalValue"]').val(subTotalAmount);
        // var queryString = "?supplierCode=" + supplierCode + "&supplierName="
        // + supplierName + "&purchaseDate=" + sPurchaseDate;

        // $.ajax({
        // beforeSend: function() {
        // $('#spinner').show()
        // },
        // complete: function() {
        // $('#spinner').hide();
        // },
        // type: "get",
        // async: false,

        // url: myContextPath + "/stock/invoiceNo" + queryString,
        // contentType: "application/json",
        // success: function(data) {
        // $('#modal-confirm').find('#input-invoice-no').val(data.data);
        // }
        // });

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#save-invoice', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        if (!$('#purchase-invoice-save').valid()) {
            return;
        }
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        autoNumericSetRawValue($(selectedRows).find('input.autonumber'))
        var invoiceTotal = Number($('#modal-confirm').find('input[name="invoiceTotal"]').autoNumeric('get'));
        var subTotalValue = $('#modal-confirm').find('input[name="subTotalValue"]').val();
        if (invoiceTotal != subTotalValue) {
            alert($.i18n.prop('page.purchased.invoice.amount.mismatch'));
            return false;
        }
        var objectArr = [];
        var object;
        var data = {};
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = $(this.node());
            object = getFormData($(data).find('input,select'));
            let remarks = $('#remarks').val();
            object['remarks'] = remarks;
            objectArr.push(object);
        });

        // let invoiceNo = $('#input-invoice-no').val();
        let dueDate = $('#input-invoice-due-date').val();
        let auctionRefNo = $('#auctionRefNo').val();
        console.log(auctionRefNo);
        // data['stockList'] = objectArr;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/stock/purchased/confirm?dueDate=" + dueDate + "&auctionRefNo=" + auctionRefNo,
            contentType: "application/json",
            success: function(data) {
                $('#modal-confirm').modal('toggle');
                table.ajax.reload();
                let alertBlockEle = $('#alert-block');
                setTimeout(function() {
                    $(alertBlockEle).removeClass('hidden');
                    $(alertBlockEle).find('span').html('Purchased Confirmed for ' + data.data);
                    ;
                }, 1000);

            }
        });
    })

    $('#close-alert-block').on('click', function() {
        $('#alert-block').addClass('hidden')
    })

    $('#save-purchased').on('click', function(e) {
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        var flag = false;
        var purFlag = false;
        var otherFlag = false;

        $.each(selectedRows, function(index, element) {
            let tr = $(element).closest('tr')
            let hiddenPurCost = getAutonumericValue($(tr).find('input[name="hiddenPurchaseTaxAmt"]'));
            let purchaseCostTaxAmount = getAutonumericValue($(tr).find('input[name="purchaseCostTaxAmount"]'));
            var purCostTaxAmtDiff = hiddenPurCost - purchaseCostTaxAmount;
            let hiddenCommisionCost = getAutonumericValue($(tr).find('input[name="hiddenCommisionTaxAmt"]'));
            let commisionCostTaxAmount = getAutonumericValue($(tr).find('input[name="commisionTaxAmount"]'));
            var commisionTaxAmtDiff = hiddenCommisionCost - commisionCostTaxAmount;
            let hiddenOthersCost = getAutonumericValue($(tr).find('input[name="hiddenOthersTaxAmt"]'));
            let othersTaxAmount = getAutonumericValue($(tr).find('input[name="othersCostTaxAmount"]'));
            var othersTaxAmtDiff = hiddenOthersCost - othersTaxAmount;
            if (purCostTaxAmtDiff == 1 || purCostTaxAmtDiff == -1 || purCostTaxAmtDiff == 0) {
                $(tr).find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set', purchaseCostTaxAmount);
            } else {
                $(tr).find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenPurCost);
                purFlag = true;
                $(tr).find('input[name="purchaseCostTaxAmount"]').css("border", "1px solid #FA0F1B");
            }
            if (commisionTaxAmtDiff == 1 || commisionTaxAmtDiff == -1 || commisionTaxAmtDiff == 0) {
                $(tr).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('set', commisionCostTaxAmount);
            } else {
                $(tr).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenCommisionCost);
                flag = true;
                $(tr).find('input[name="commisionTaxAmount"]').css("border", "1px solid #FA0F1B");

            }
            if (othersTaxAmtDiff == 1 || othersTaxAmtDiff == -1 || othersTaxAmtDiff == 0) {
                $(tr).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('set', othersTaxAmount);
                otherFlag = false;
            } else {
                $(tr).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenOthersCost);
                otherFlag = true;
                $(tr).find('input[name="othersCostTaxAmount"]').css("border", "1px solid #FA0F1B");

            }

        });

        if (flag || purFlag || otherFlag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            return false;
        }
    })

    $('#save-purchased-costs').on('click', function(e) {

        let relatedTarget = $(e.relatedTarget);
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        })

        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        var selectedRowsData = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        var flag = false;
        var purFlag = false;
        var otherFlag = false;

        $.each(selectedRowsData, function(index, element) {
            let tr = $(element).closest('tr')
            let hiddenPurCost = getAutonumericValue($(tr).find('input[name="hiddenPurchaseTaxAmt"]'));
            let purchaseCostTaxAmount = getAutonumericValue($(tr).find('input[name="purchaseCostTaxAmount"]'));
            var purCostTaxAmtDiff = hiddenPurCost - purchaseCostTaxAmount;
            let hiddenCommisionCost = getAutonumericValue($(tr).find('input[name="hiddenCommisionTaxAmt"]'));
            let commisionCostTaxAmount = getAutonumericValue($(tr).find('input[name="commisionTaxAmount"]'));
            var commisionTaxAmtDiff = hiddenCommisionCost - commisionCostTaxAmount;
            let hiddenOthersCost = getAutonumericValue($(tr).find('input[name="hiddenOthersTaxAmt"]'));
            let othersTaxAmount = getAutonumericValue($(tr).find('input[name="othersCostTaxAmount"]'));
            var othersTaxAmtDiff = hiddenOthersCost - othersTaxAmount;
            if (purCostTaxAmtDiff == 1 || purCostTaxAmtDiff == -1 || purCostTaxAmtDiff == 0) {
                $(tr).find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set', purchaseCostTaxAmount);
                purFlag = false;
            } else {
                $(tr).find('input[name="purchaseCostTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenPurCost);
                purFlag = true;
                $(tr).find('input[name="purchaseCostTaxAmount"]').css("border", "1px solid #FA0F1B");
            }
            if (commisionTaxAmtDiff == 1 || commisionTaxAmtDiff == -1 || commisionTaxAmtDiff == 0) {
                $(tr).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('set', commisionCostTaxAmount);
                flag = false;
            } else {
                $(tr).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenCommisionCost);
                flag = true;
                $(tr).find('input[name="commisionTaxAmount"]').css("border", "1px solid #FA0F1B");

            }
            if (othersTaxAmtDiff == 1 || othersTaxAmtDiff == -1 || othersTaxAmtDiff == 0) {
                $(tr).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('set', othersTaxAmount);
                otherFlag = false;
            } else {
                $(tr).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('set', hiddenOthersCost);
                otherFlag = true;
                $(tr).find('input[name="othersCostTaxAmount"]').css("border", "1px solid #FA0F1B");

            }

        });

        if (flag || purFlag || otherFlag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            return e.preventDefault();
        }

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }

        var rowDatas = selectedRows.data();

        var isValid = isValidSelection(rowDatas);
        if (!isValid) {
            alert($.i18n.prop('page.purchased.invoice.confirm.validation.singleinvoice'))
            return e.preventDefault();
        }

        if (!$('#form-purchased').valid() || !isPurchaseEntryValid($(selectedRows.nodes()))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return e.preventDefault();
        } else {// $(this).closest('tr').find('#check-box-select');
        }

        autoNumericSetRawValue($(selectedRowsData).find('input.autonumber'))
        var objectArr = [];
        var object;
        // var data = {};
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = $(this.node());
            var rowdata = table.row(rowIdx).data();
            object = getFormData($(data).find('input,select'));
            object['id'] = rowdata.code;
            objectArr.push(object);
        });
        // data['stockList'] = objectArr;
        // console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/stock/purchased/save",
            contentType: "application/json",
            success: function(objectArr) {
                table.ajax.reload();
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Stock saved.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
            }
        });
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
                // posNosArr=item.
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
        table.draw();
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
        table.draw();
    })

    $('#table-purchased').on('click', '#cancel-purchase', function() {
        var stockNo;
        var rowData = table.row($(this).closest('tr')).data();
        stockNo = rowData.stockNo;
        var cancelPurchase = false;
        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return;
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/stock/purchased/cancel-update?stockNo=" + stockNo,
            success: function(data) {
                table.ajax.reload();
                $('#input-invoice-no').val('');
                $('#input-invoice-due-date').val('');
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Purchase cancelled.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
                //set status
                setShippingDashboardStatus();
            }
        });
    }).on('click', '#delete-invoice-item', function() {
        var invoiceNo;
        var rowData = table.row($(this).closest('tr')).data();
        invoiceNo = rowData.code;
        var queryString = "?invoiceNo=" + invoiceNo + "&type=" + rowData.type
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "delete",
            url: myContextPath + "/stock/purchased/invoice/delete" + queryString,
            success: function(data) {
                table.ajax.reload();
                alertMessage($('#alert-block'), '<strong>Success!</strong> Purchase cancelled.');
                //set status
                setShippingDashboardStatus();
            }
        });
    })

    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();

    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD/MM/YYYY')._d.getTime();
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
        // purchase type filter
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[15].length == 0 || aData[15] != filterPurchaseType) {
                return false;
            }
        }
        // Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[16].length == 0 || aData[16] != filterSupplierName) {
                return false;
            }
        }

        // Auction Housee filter

        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[17].length == 0 || !aData[17].match(auctionHouseFilterRegex)) {
                return false;
            }
        }

        // /Pos No filter
        if (typeof filterPosNo != 'undefined' && filterPosNo.length != '') {
            if (aData[5].length == 0 || aData[5] != filterPosNo) {
                return false;
            }
        }
        return true;
    });

    $('#table-purchased').on('click', '#edit-purchase', function() {
        var stockNo;
        var rowData = table.row($(this).closest('tr')).data();
        stockNo = rowData.stockNo;
        if (!isEmpty(stockNo)) {
            $.redirect(myContextPath + '/stock/stock-entry/' + stockNo, {}, "GET");
        }

    })
    $('#table-purchased').on('click', '#accounts-edit-purchase', function() {
        var stockNo;
        var rowData = table.row($(this).closest('tr')).data();
        stockNo = rowData.stockNo;
        if (!isEmpty(stockNo)) {

            let params = {};
            params.editFlag = 1;
            params.return = window.location.pathname;

            $.redirect(myContextPath + '/accounts/stock-entry/' + stockNo, params, "GET");
        }

    })
    var radioSchedule = $('#modal-arrange-transport').find('input[type="radio"][name="selectedtype"].minimal,input[type="radio"][name="selecteddate"].minimal');
    radioSchedule.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    // transport arrangement
    $('#modal-arrange-transport').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (table.rows('.can-transaport-arrange', {
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }
        var rowdata = table.rows('.can-transaport-arrange', {
            selected: true,
            page: 'current'
        }).data();
        // check valid
        var isValid = isValidSelection(rowdata);
        if (!isValid) {
            alert($.i18n.prop('page.purchased.invoice.confirm.validation.samepurchasedate'))
            return e.preventDefault();
        }

        for (var i = 0; i < rowdata.length; i++) {
            if (rowdata[i].transportationStatus == 1) {
                if (rowdata.length == 1) {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }
        var supplierCode = rowdata[0].supplierCode;
        var purchaseDate = rowdata[0].purchaseDate;

        var balance = getSupplierCreditBalance(supplierCode)

        $(this).find('#creditBalance.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(balance, 0));

        $(this).find('input[name="purchaseDate"]').val(purchaseDate)
        $(this).find('input[name="supplierCode"]').val(supplierCode)
        $('#pickupDate,#deliveryDate').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        }).on('change', function() {
            $(this).valid();
        })
        $(this).find('#auto-date').iCheck('check').trigger('ifChecked');

        var element;
        var i = 0;
        table.rows('.can-transaport-arrange', {
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var remarks = ifNotValid(data.remarks, '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(data.auctionInfoLotNo, '');
            var posNo = ifNotValid(data.auctionInfoPosNo, '');
            var numberPlate = ifNotValid(data.numberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort)
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var pickupLocation = ifNotValid(data.transportInfo.pickupLocation, '');
            var pickupLocationCustom = ifNotValid(data.transportInfo.pickupLocationCustom, '');
            var dropLocation = ifNotValid(data.transportInfo.dropLocation, '');
            var dropLocationCustom = ifNotValid(data.transportInfo.dropLocationCustom, '');
            var transporter = ifNotValid(data.transportInfo.transporter, '');
            var charge = ifNotValid(data.charge, '');
            var posNos = ifNotValid(data.posNos, '');
            var transportCategory = ifNotValid(data.transportCategory, '');
            // var inspectionFlag = ifNotValid(data.inspectionFlag, '');
            if (!isEmpty(data.transportationStatus) && data.transportationStatus == 0 && isEmpty(data.lastTransportLocation)) {
                pickupLocation = ifNotValid(isExistNested(data, 'transportInfo', 'pickupLocation') ? data.transportInfo.pickupLocation : '', '');
                pickupLocationCustom = ifNotValid(isExistNested(data, 'transportInfo', 'pickupLocationCustom') ? data.transportInfo.pickupLocationCustom : '', '');
                dropLocation = ifNotValid(isExistNested(data, 'transportInfo', 'dropLocation') ? data.transportInfo.dropLocation : '', '');
                dropLocationCustom = ifNotValid(isExistNested(data, 'transportInfo', 'dropLocationCustom') ? data.transportInfo.dropLocationCustom : '', '');
            } else {
                pickupLocation = ifNotValid(data.lastTransportLocation, '');
                pickupLocationCustom = ifNotValid(data.lastTransportLocationCustom, '');
                dropLocation = '';
                dropLocationCustom = '';
            }

            // var transporter = ifNotValid(data.transporter, '');

            if (i != 0) {
                element = $('#item-vechicle-clone').find('.item-vehicle').clone();

                $(element).appendTo('#item-vehicle-clone-container');
                $('.charge').autoNumeric('init');
            } else {
                element = $('#item-vehicle-container').find('.item-vehicle');
            }
            $(element).find('select.select2-select').select2({
                allowClear: true,
                width: '100%'
            });

            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('textarea[name="remarks"]').val(remarks);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('input[name="posNo"]').val(posNo);
            $(element).find('input[name="numberPlate"]').val(numberPlate);
            $(element).find('input[name="data"]').val(transporter);
            $(element).find('input[name="hiddencharge"]').val(charge);
            $(element).find('input[name="transportCategory"]').val(transportCategory);
            $(element).find('select[name="destinationCountry"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(countryJson, function(item) {
                    return {
                        id: item.country,
                        text: item.country,
                        data: item
                    };
                })
            }).val(destinationCountry).trigger('change');
            if (data.auctionHouse == null) {
                $(element).find('.hidePosNo').addClass('hidden');
            }
            $(element).find('select[name="posNo"]').empty()
            $(element).find('select[name="posNo"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(posNos, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val(posNo).trigger('change');
            $(element).find('select[name="destinationPort"]').val(destinationPort).trigger('change');
            $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger('change');
            $(element).find('select[name="dropLocation"]').val(dropLocation).trigger('change');
            $(element).find('select[name="transporter"]').val(transporter).trigger('change');
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);
            // if (inspectionFlag == 1) {
            // $(element).find('input[name="inspectionFlag"]').attr('checked',
            // true);
            // } else {
            // $(element).find('input[name="inspectionFlag"]').attr('checked',
            // false);
            // }

            var closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
            if (pickupLocation.toLowerCase() === 'others') {
                closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(pickupLocationCustom)
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            closest_container = $(element).find('select[name="dropLocation"]').closest('.form-group');
            if (dropLocation.toLowerCase() === 'others') {
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(dropLocationCustom)
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="dropLocation"]').val(dropLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }

            $(element).find('input[name="charge"]').autoNumeric('init').autoNumeric('set', ifNotValid(charge, 0));
            i++;
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-vehicle-container').find('#item-vehicle-clone-container').html('');
        $(this).find('.hidePosNo').removeClass('hidden');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[name="pickupDate"]').val(['']);
        $(this).find('#transport-schedule-details').find('input[name="deliveryDate"]').val(['']);
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2').val('').trigger('change');
    }).on('change', '.select2-select.with-others', function() {
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.others-input-container').removeClass('hidden');
            $(closest_container).find('span.select2').addClass('hidden');
        }
    }).on('ifChecked', 'input[name="selecteddate"]', function() {
        if ($(this).val() == 0) {
            $('#transport-schedule-details').find('#schedule-date').addClass('hidden')
        } else if ($(this).val() == 1) {
            $('#transport-schedule-details').find('#schedule-date').removeClass('hidden')
            var purchaseDate = ifNotValid($('#modal-arrange-transport').find('input[name="purchaseDate"]').val());
            var supplierCode = ifNotValid($('#modal-arrange-transport').find('input[name="supplierCode"]').val());
            var response = getInvoiceDueDate(supplierCode);
            var maxDueDays = response.data.maxDueDays;
            var paymentDate = moment(purchaseDate, "DD-MM-YYYY").add(maxDueDays, 'days').format('DD-MM-YYYY');
            var pickupDate = moment(paymentDate, "DD-MM-YYYY").add(1, 'days').format('DD-MM-YYYY');
            $('#deliveryDate').val(paymentDate).datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            }).on('change', function() {
                $(this).valid();
            });
            $('#pickupDate').datepicker('setDate', pickupDate);
            $('#deliveryDate').datepicker('setDate', paymentDate);

        }
    }).on('click', 'a.show-dropdown', function() {
        var closest_container = $(this).closest('.form-group');
        $(closest_container).find('select.select2-select').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('span.select2').removeClass('hidden');
        $(closest_container).find('div.others-input-container').addClass('hidden');

    }).on('change', 'select[name="pickupLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: !isEmpty(item.amount) ? item.name + ' [ ¥ ' + ifNotValid(item.amount, '') + ' ]' : item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');

    }).on('change', 'select[name="dropLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: !isEmpty(item.amount) ? item.name + ' [ ¥ ' + ifNotValid(item.amount, '') + ' ]' : item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');
    }).on('change', 'select[name="transporter"]', function() {
        var charge = $(this).closest('.item-vehicle').find('input[name="charge"]');
        var valData = $(this).find('option:selected').data('data');
        if (!isEmpty(valData)) {
            if (!isEmpty(valData.data)) {
                $(charge).autoNumeric('init').autoNumeric('set', ifNotValid(valData.data.amount, 0));
            }
        } else {
            $(charge).autoNumeric('init').autoNumeric('set', 0);
        }
    }).on('change', 'select[name="destinationCountry"]', function() {
        var data = $(this).find(':selected').data('data');
        var destinationPort = $(this).closest('.item-vehicle').find('select[name="destinationPort"]');
        destinationPort.empty();
        data = data.data;
        if (!isEmpty(data)) {
            destinationPort.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item

                    };
                })
            }).val('').trigger("change");
            // $(this).closest('.item-vehicle').find('input[name="inspectionFlag"]').attr('checked',
            // data.inspectionFlag == 1 ? true : false);
        }
    });
    // Save Arrange Transport
    $('#btn-create-transport-order').on('click', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        if (!$('#transport-arrangement-form').find('.valid-required-transport-fields, .posNo').valid()) {
            return;
        }
        var objectArr = [];
        var data = {};
        autoNumericSetRawValue($('#modal-arrange-transport').find('.charge'))
        var scheduleDetails = getFormData($('#transport-schedule-details').find('.schedule-data'));
        var selectedType = getFormData($('#transport-schedule-details').find('.selected-type'));
        var transportComment = getFormData($('#transport-comment').find('.comment'));
        $("#item-vehicle-container").find('.item-vehicle').each(function() {
            var object = {};
            object = getFormData($(this).find('input,select,textarea'));
            // let isChecked =
            // $(this).find('input[name="inspectionFlag"]').is(":checked");
            // if (isChecked) {
            // object.inspectionFlag = 1
            // } else {
            // object.inspectionFlag = 0
            // }
            object.pickupDate = scheduleDetails.pickupDate;
            object.pickupTime = scheduleDetails.pickupTime;
            object.deliveryDate = scheduleDetails.deliveryDate;
            object.scheduleType = selectedType.selectedtype;
            object.selectedDate = scheduleDetails.selecteddate;
            object.comment = transportComment.comment;
            object.deliveryTime = scheduleDetails.deliveryTime;
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
            url: myContextPath + "/transport/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    $('#modal-arrange-transport').modal('toggle');
                }

            }
        });
    })

    // stock details modal update
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBody.slimScroll({
        start: 'bottom',
        railVisible: true,
        size: '5px',
        position: 'right',
        distance: '10px',
        height: '720px'
    });
    stockDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        stockCloneElement.clone().appendTo(stockDetailsModalBody);
        // updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
    })

    let modal_add_element = $('#modal-add-payment')
    modal_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        $.getJSON(myContextPath + "/data/auctionPayment.json", function(data) {
            $('#modal-add-payment #invoiceItemType').select2({
                allowClear: true,
                width: "100%",
                data: $.map(data, function(item) {
                    return {
                        id: item.code,
                        text: item.type,
                        data: item
                    };
                })
            }).val('').trigger("change");
        })
        modalAddElementTriggerEle = $(event.relatedTarget);
        var rowData = table.row($(modalAddElementTriggerEle).closest('tr')).data();
        if (!isEmpty(rowData) && rowData.type == "PURCHASE") {
            modal_add_element.find('#invoiceDate').val(rowData.purchaseDate);
            modal_add_element.find('#purchasedSupplier').val(rowData.supplierCode).trigger('change');
            modal_add_element.find('#purchasedAuctionHouse').val(rowData.auctionHouseId).trigger('change');
        }
        modal_add_element.find('input[name=taxAmount]').val(0);
        modal_add_element.find('input[name=totalAmount]').val(0);
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
    }).on('change', 'input[name="amount"]', function() {
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));

        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = amount * taxPer / 100;
        let isTax = modal_add_element.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {}
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on('keyup', '.taxPercent', function(e) {
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt;
        let isTax = modal_add_element.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (totalAmt / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            taxAmt = amount * taxPer / 100;
        }
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on('click', '.taxInclusive', function(e) {
        var taxAmt = Number(ifNotValid(modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        if ($(this).is(':checked')) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;
        }
        totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on("keyup", "input.autonumber", function(setting) {
        // update subtotal
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = Number(ifNotValid(modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt);
    }).on('change', 'select#purchasedSupplier', function(event) {
        if (isEmpty($(this).val())) {
            $('select#purchasedAuctionHouse').empty();
            return;
        }
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var auctionHouseArr = data[0].data.supplierLocations;
            modal_add_element.find('#purchasedAuctionHouse').empty().select2({
                placeholder: "Select Auction House",
                allowClear: true,
                data: $.map(auctionHouseArr, function(item) {
                    return {
                        id: item.id,
                        text: item.auctionHouse,
                        data: item
                    };
                })
            }).val('').trigger("change");

        }

    }).on('click', 'button#add-payment', function() {
        var amtFlag = false;
        if (!$('#modal-add-payment form#formAddPayment').valid()) {
            return;
        }
        let amountValue = Number(getAutonumericValue(modal_add_element.find('input[name="amount"]')));
        let totalAmountValue = Number(getAutonumericValue(modal_add_element.find('input[name="totalAmount"]')));
        let hiddenTax = Number(getAutonumericValue(modal_add_element.find('input[name="hiddenTaxAmount"]')));
        let orgTaxAmount = Number(getAutonumericValue(modal_add_element.find('input[name="taxAmount"]')));
        var taxDiff = hiddenTax - orgTaxAmount;
        var totalValue = 0;
        if (taxDiff == 1 || taxDiff == -1 || taxDiff == 0) {
            totalValue = amountValue + orgTaxAmount;
            modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', orgTaxAmount);
            modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
        } else {
            totalValue = amountValue + hiddenTax;
            modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', hiddenTax);
            modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
            amtFlag = true;
            modal_add_element.find('input[name="taxAmount"]').css("border", "1px solid #FA0F1B");
        }
        if (amtFlag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        let invoiceType = modal_add_element.find('select[name="invoiceItemType"]').val();
        let purchasedSupplier = modal_add_element.find('select[name="purchasedSupplier"]').val();
        let amount = getAutonumericValue(modal_add_element.find('input[name="amount"]'));
        let othersTaxValue = getAutonumericValue(modal_add_element.find('input[name="taxPercent"]'));
        let othersCostTaxAmount = getAutonumericValue(modal_add_element.find('input[name="taxAmount"]'));
        let othersTotal = getAutonumericValue(modal_add_element.find('input[name="totalAmount"]'));
        let invoiceDate = modal_add_element.find('input[name="invoiceDate"]').val();
        let purchasedAuctionHouse = modal_add_element.find('select[name="purchasedAuctionHouse"]').val();
        let cancelledStock;
        let takeOutStocks;
        let claimed;
        let url;
        if (invoiceType == 5)
            cancelledStock = modal_add_element.find('select[name="cancelledStock"]').val();
        else if (invoiceType == 9)
            cancelledStock = modal_add_element.find('select[name="reauctionStock"]').val();
        else if (invoiceType == 7)
            cancelledStock = modal_add_element.find('select[name="transportStock"]').val();
        else if (invoiceType == 10)
            cancelledStock = modal_add_element.find('select[name="penaltyStock"]').val();
        else if (invoiceType == 12)
            cancelledStock = modal_add_element.find('select[name="recycleCarTaxPaidStock"]').val();
        else if (invoiceType == 13)
            cancelledStock = modal_add_element.find('select[name="recycleCarTaxPaidStock"]').val();
        else if (invoiceType == 14)
            takeOutStocks = modal_add_element.find('select[name="takeOutStock"]').val();
        else if (invoiceType > 14)
            cancelledStock = modal_add_element.find('select[name="stockVisible"]').val();
        claimed = modal_add_element.find('input[name="claimedStatus"]').val();

        let data = {};
        data['invoiceItemType'] = invoiceType;
        data['supplier'] = purchasedSupplier;
        data['amount'] = amount;
        data['othersTaxValue'] = othersTaxValue;
        data['othersCostTaxAmount'] = othersCostTaxAmount;
        data['othersTotal'] = othersTotal;
        data['invoiceDate'] = invoiceDate;
        data['purchasedAuctionHouse'] = purchasedAuctionHouse;
        data['stockNo'] = cancelledStock;
        data['claimed'] = claimed;
        url = "/stock/purchased/invoice/addPayment";
        if (invoiceType == 14) {
            data['stockNos'] = takeOutStocks;
            url = "/stock/purchased/invoice/addPayment/takeOutStocks"
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + url,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    modal_add_element.modal('toggle');
                    alertMessage($('#alert-block'), '<strong>Success!</strong> Payment added')
                }

            }
        });
    }).on('change', 'select[name="invoiceItemType"]', function() {
        let type = $(this).val();
        var data = $(this).find(':selected').data('data');
        let stockElementWrapper = $('#modal-add-payment div#cancelledStockWrapper');
        let reauctionElementWrapper = $('#modal-add-payment div#reauctionStockWrapper');
        let rikusoElementWrapper = $('#modal-add-payment div#rikusoStockWrapper');
        let penaltyElementWrapper = $('#modal-add-payment div#penaltyStockWrapper');
        let recycleCarTaxPaidStockWrapper = $('#modal-add-payment div#recycleCarTaxPaidStockWrapper');
        let stockViewWrapper = $('#modal-add-payment div#stockViewWrapper');
        let takeOutStockWrapper = $('#modal-add-payment div#takeOutStockWrapper');
        if (!isEmpty(type) && (type == 5)) {
            stockElementWrapper.removeClass('hidden');

        } else {
            stockElementWrapper.addClass('hidden');
        }
        if (!isEmpty(type) && (type == 9)) {
            reauctionElementWrapper.removeClass('hidden');

        } else {
            reauctionElementWrapper.addClass('hidden');
        }
        if (!isEmpty(type) && (type == 7)) {
            rikusoElementWrapper.removeClass('hidden');

        } else {
            rikusoElementWrapper.addClass('hidden');
        }
        if (!isEmpty(type) && (type == 10)) {
            penaltyElementWrapper.removeClass('hidden');

        } else {
            penaltyElementWrapper.addClass('hidden');
        }

        if (!isEmpty(type) && (type == 12) || (type == 13)) {
            recycleCarTaxPaidStockWrapper.removeClass('hidden');

        } else {
            recycleCarTaxPaidStockWrapper.addClass('hidden');
        }
        if (!isEmpty(type) && (type == 14)) {
            takeOutStockWrapper.removeClass('hidden');

        } else {
            takeOutStockWrapper.addClass('hidden');
        }
        if (!isEmpty(type) && (type > 14)) {
            $('#modal-add-payment').find('input[name="claimedStatus"]').val(data.data.claimed);
            if (!isEmpty(data.data)) {
                if (data.data.stockView == 1) {
                    stockViewWrapper.removeClass('hidden');

                } else {
                    stockViewWrapper.addClass('hidden')
                    $('select[name="stockVisible"]').val('').trigger('change');
                }
            } else {
                stockViewWrapper.addClass('hidden')
                $('select[name="stockVisible"]').val('').trigger('change');
            }

        } else {
            stockViewWrapper.addClass('hidden');
            $('select[name="stockVisible"]').val('').trigger('change');
        }
        stockElementWrapper.find('select[name="cancelledStock"]').val('').trigger('change');

        console.log(stockUrl);
    })

    $("#checkbox").click(function() {
        if ($("#checkbox").is(':checked')) {
            $("#takeOutStock> option").prop("selected", "selected");
            $("#takeOutStock").trigger("change");
        } else {
            $("#takeOutStock > option").remove();
            $("#takeOutStock").trigger("change");
        }
    });

})
function getTransporter(pickupLocEle, dropLocEle) {
    var pickupLoc = pickupLocEle.val();
    var dropLoc = dropLocEle.val();
    var autopopulate = false;
    var result = [];
    if (pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() == 'OTHERS') {
        result = transportersJson;
    } else if ((pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') || (dropLoc.toUpperCase() == 'OTHERS' && pickupLoc.toUpperCase() != 'OTHERS')) {
        result = transportersJson;
    } else if (pickupLoc.toUpperCase() != 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') {
        var response = getTransporterFee(pickupLocEle);
        result = response.data.list;
        autopopulate = response.data.autopopulate;
    }
    var data = {};
    data["autopopulate"] = autopopulate;
    data["transportArr"] = result;
    return data;
}
function getTransporterFee(element) {
    var response;
    var closest_container = element.closest('.item-vehicle');
    var transportCategory = closest_container.find('input[name="transportCategory"]').val();
    var from = closest_container.find('select[name="pickupLocation"]').val()
    var to = closest_container.find('select[name="dropLocation"]').val()
    var queryString = "?transportCategory=" + transportCategory + "&from=" + from + "&to=" + to
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/transport/charge" + queryString,
        success: function(data) {
            response = data;
        }
    })
    return response;
}

function addPurchasedListValidations(element) {
    $(element).find('input[name="purchaseCost"],input[name="commision"],input[name="recycle"]').addClass('validate-required');
}
function removePurchasedListValidations(element) {
    $(element).find('input[name="purchaseCost"],input[name="commision"],input[name="recycle"]').removeClass('validate-required');

}

function isPurchaseEntryValid(element) {
    var valid = true;
    // valid =
    // $(element).find('input[name="purchaseCost"],input[name="commision"],input[name="recycle"]').valid();
    valid = elementValueNotEmpty($(element).find('input[name="purchaseCost"],input[name="commision"],input[name="recycle"]'));
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
function getSupplierCreditBalance(supplierCode) {
    var balance;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/supplier/credit/balance/" + supplierCode,
        success: function(data) {
            balance = data.data;
        }
    })
    return balance;
}
function isPickupAndDropIsSame(element) {
    var container = $(element).closest('.item-vehicle')
    var from = container.find('select[name="pickupLocation"]').val();
    var to = container.find('select[name="dropLocation"]').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}
