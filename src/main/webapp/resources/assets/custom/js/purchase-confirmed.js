var locationJson, forwardersJson, categoriesJson, transportersJson, supplierJson, countryJson;
$(function() {
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
    //set status
    setShippingDashboardStatus();
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

    //AutoNumeric
    $('.charge').autoNumeric('init');

    //populate dropdown options
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
    })
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;
        $('#transport-items').find('select[name="forwarder"]').select2({
            placeholder: "Select forwarder address",
            allowClear: true,
            width: '100%',
            data: $.map(forwardersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
        $('#item-vechicle-clone').find('select[name="forwarder"]').select2('destroy')
    })
    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        categoriesJson = data;
        $('#transport-items').find('select[name="category"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(categoriesJson, function(item) {
                var childrenArr = [];
                $.each(item.subCategories, function(i, val) {
                    childrenArr.push({
                        id: val.name,
                        text: val.name
                    })
                })
                return {
                    text: item.name,
                    children: childrenArr
                };
            })
        })

        $('#item-vechicle-clone').find('select[name="category"]').select2('destroy')
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
        }).val('').trigger('change');
        $('#item-vechicle-clone').find('select[name="transporter"]').select2('destroy')
    })

    $('#table-month-filter').on('change', function() {
        var select = $('select[name="table-month-filter"]').find('option:selected').val();
        if (select == "period") {
            $('div#period-data').removeClass('hidden');
            if (isEmpty($('input#table-filter-from').val()) || isEmpty($('input#table-filter-to').val())) {
                return;
            }
        } else {
            $('div#period-data').addClass('hidden');
            $('div#period-data').find('input').val('');
        }
        table.ajax.reload();
    })

    $('#btn-search-data').on('click', function() {
        if (isEmpty($('input#table-filter-from').val()) || isEmpty($('input#table-filter-to').val())) {
            alert("please fill required fields");
            $('input#table-filter-from,input#table-filter-to').css("border", "1px solid #FA0F1B");
            return;
        } else {
            table.ajax.reload();
        }
    })

    var from;
    $('#table-filter-from,#table-filter-to').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    })

    // Select 2
    $('#table-filter-auctionhouse').select2({
        placeholder: "All",
        allowClear: true
    })
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    var table = $('#table-purchaseConfirmed').DataTable({

        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "aaSorting": [[0, "desc"]],
        "pageLength": 25,
        "ajax": {
            beforeSend: function() {
                let spinner = $('#spinner').clone()
                spinner.attr('id', 'datatableSpinner');
                $('body').append(spinner);
                spinner.show();
            },
            complete: function() {
                $('#datatableSpinner').remove();
            },
            'url': myContextPath + "/stock/purchaseconfirmed-data/month-wise",
            'data': function(data) {
                var selected = $('select[name="table-month-filter"]').find('option:selected').val();
                data["period"] = selected;
                data["from"] = $('input#table-filter-from').val();
                data["to"] = $('input#table-filter-to').val();

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
            orderable: false,
            searchable: false,
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
            "data": "sPurchaseInfoDate",
            "render": function(data, type, row) {
                data = data == null ? '' : data;

                return data;
            }
        }, {
            targets: 2,
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
            "data": "model"
        }, {
            targets: 4,
            "data": "prchsInfoLotNo"
        }, {
            targets: 5,
            "data": "prchsInfoPosNo"
        }, {
            targets: 6,
            "data": "prchsInfoSSupplier",
        }, {
            targets: 7,
            "data": "prchsInfoAuctionHouse",
            "visible": false
        }, {
            targets: 8,

            "data": "trnsprtInfoPickupLocationName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.trnsprtInfoPickupLocationCustom
                } else {
                    return data;
                }
            }
        }, {
            targets: 9,

            "data": "trnsprtInfoDropLocationName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.trnsprtInfoDropLocationCustom
                } else {
                    return data;
                }
            }
        }, {
            targets: 10,
            "data": "oldNumberPlate"
        }, {
            targets: 11,
            "data": "destinationCountry"
        }, {
            targets: 12,
            "data": "prchsInfoType",
            "visible": false
        }, {
            targets: 13,
            className: 'align-center',
            "data": "trnsprtInfostatus",
            "render": function(data, type, row) {
                var status;
                var className = "info";
                if (data == "1") {
                    status = "Requested"
                } else if (data == "2") {
                    status = "Confirmed"
                } else if (data == "4") {
                    status = "Rearrangement"
                } else if (data == "5") {
                    status = "Delivery Confirm"
                } else if (data == "6") {
                    status = "Delivered"
                } else if (data == "7") {
                    status = "In Transit"
                } else {
                    status = "Idle"
                }
//                 if (row.chassisNo == "M900A-0034304") {
//                     console.log(status + "" + className)
//                 }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 14,
            "data": "stockNo",
            "render": function(data, type, row) {
                var html = '';
                //                 html = '<a type="button" class="btn btn-danger ml-5 btn-xs" title="Cancel Purchase" id="cancel-purchase"><i class="fa fa-fw fa-close"></i></a>'
                html += '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Edit Purchase" id="edit-purchase"><i class="fa fa-fw fa-edit"></i></a>'
                return '<div style="width:100px;">' + html + '</div>';
            }

        }, {
            targets: 15,
            "data": "prchsInfoSupplier",
            "visible": false //"searchable": false
        }, {
            targets: 16,
            "data": "prchsInfoSAuctionHouse",
            "visible": false //"searchable": false
        }]

    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
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
    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // date filter
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
        // purchase type filter
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[12].length == 0 || aData[12] != filterPurchaseType) {
                return false;
            }
        }
        // Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[15].length == 0 || aData[15] != filterSupplierName) {
                return false;
            }
        }

        // Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[16].length == 0 || aData[16] != filterAuctionHouse) {
                return false;
            }
        }
        ///Pos No filter
        if (typeof filterPosNo != 'undefined' && filterPosNo.length != '') {
            if (aData[5].length == 0 || aData[5] != filterPosNo) {
                return false;
            }
        }
        return true;
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

    });

    // filter operations
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

    //Arrange Transporte Modal
    var radioSchedule = $('#modal-arrange-transport').find('input[type="radio"][name="selectedtype"].minimal,input[type="radio"][name="selecteddate"].minimal');
    radioSchedule.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    var selectedRow;
    $('#modal-arrange-transport').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();

        }
        var rowdata = table.rows({
            selected: true,
            page: 'current'
        }).data();
        var isValid = isValidSelection(rowdata);

        //check valid
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
        var isValid = isValidSelection(rowdata);
        var isCount = isCountSelection(rowdata);
        var isCompleteCount = isCompleteCountSelection(rowdata);
        if (isCompleteCount && isCount) {
            $('#modal-arrange-transport').find('.modal-title').find('span.pull-right').addClass('hidden');
        } else if (!(isCount)) {
            alert($.i18n.prop('alert.stock.transport.validation.already.count.selection'));
            return e.preventDefault();
        } else if (!isValid) {
            alert($.i18n.prop('page.purchased.invoice.confirm.validation.samepurchasedate'))
            return e.preventDefault();
        }

        var supplierCode = rowdata[0].prchsInfoSupplier;
        var purchaseDate = rowdata[0].purchaseInfoDate;
        $(this).find('input[name="purchaseDate"]').val(purchaseDate)
        $(this).find('input[name="supplierCode"]').val(supplierCode)
        var balance = getSupplierCreditBalance(supplierCode)
        $(this).find('#creditBalance.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(balance, 0));

        $(this).find('#auto-date').iCheck('check').trigger('ifChecked');

        var element;
        var i = 0;
        selectedRow = table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();

            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(isExistNested(data, 'prchsInfoLotNo') ? data.prchsInfoLotNo : '', '');
            var posNo = ifNotValid(data.prchsInfoPosNo, '');
            var numberPlate = ifNotValid(data.oldNumberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort, '');
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var charge = ifNotValid(data.charge, '');
            var pickupLocation = '';
            var pickupLocationCustom = '';
            var dropLocation = '';
            var dropLocationCustom = '';
            var posNos = ifNotValid(data.posNos, '');
            var transportCategory = ifNotValid(data.transportCategory, '');
            //var inspectionFlag = ifNotValid(data.inspectionFlag, '');

            if (!isEmpty(data.transportationStatus) && data.transportationStatus == 0 && isEmpty(data.lastTransportLocation)) {
                pickupLocation = ifNotValid(isExistNested(data, 'trnsprtInfoPickupLocation') ? data.trnsprtInfoPickupLocation : '', '');
                pickupLocationCustom = ifNotValid(isExistNested(data, 'trnsprtInfoPickupLocationCustom') ? data.trnsprtInfoPickupLocationCustom : '', '');
                dropLocation = ifNotValid(isExistNested(data, 'trnsprtInfoDropLocation') ? data.trnsprtInfoDropLocation : '', '');
                dropLocationCustom = ifNotValid(isExistNested(data, 'trnsprtInfoDropLocationCustom') ? data.trnsprtInfoDropLocationCustom : '', '');
            } else {
                pickupLocation = ifNotValid(data.lastTransportLocation, '');
                pickupLocationCustom = ifNotValid(data.lastTransportLocationCustom, '');
                dropLocation = '';
                dropLocationCustom = '';
            }

            var transporterData = ifNotValid(data.transporter, '');

            if (i != 0) {
                element = $('#item-vechicle-clone').find('.item-vehicle').clone();

                $(element).appendTo('#item-vehicle-clone-container');
                $('.charge').autoNumeric('init');
            } else {
                element = $('#item-vehicle-container').find('.item-vehicle');

            }
            if (!(data.transportationCount == 0) && !(data.prchsInfoAuctionHouse == null)) {
                $(element).find('input[name="lotNo"]').attr('readonly', false);
            } else {
                $(element).find('input[name="lotNo"]').attr('readonly', true);
            }
            $(element).find('select.select2-select').select2({
                allowClear: true,
                width: '100%'
            });
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('select[name="posNo"]').val(posNo).trigger('change');
            $(element).find('input[name="numberPlate"]').val(numberPlate);
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
            if (data.prchsInfoAuctionHouse == null) {
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
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);
            $(element).find('input[name="data"]').val(transporterData);
            //            if (inspectionFlag == 1) {
            //                $(element).find('input[name="inspectionFlag"]').attr('checked', true);
            //            } else {
            //                $(element).find('input[name="inspectionFlag"]').attr('checked', false);
            //            }

            var closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
            if (pickupLocation.toLowerCase() === 'others') {
                closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(pickupLocationCustom)
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            closest_container = $(element).find('select[name="dropLocation"]').closest('.form-group');
            if (dropLocation.toLowerCase() === 'others') {
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(dropLocationCustom)
                $(element).find('select[name="dropLocation"]').val(dropLocation).trigger("change");
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
        $(this).find('#transport-schedule-details').find('input[name="selecteddate"][value="2"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[name="pickupDate"]').val(['']);
        $(this).find('#transport-schedule-details').find('input[name="deliveryDate"]').val(['']);
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#transport-remark').find('textarea').val('');
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2-select').val('').trigger('change');
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
        if (!isEmpty(transporterData) && !autopopulate) {
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

        }
        //$(this).closest('.item-vehicle').find('input[name="inspectionFlag"]').attr('checked', data.inspectionFlag == 1 ? true : false);
    });

    $('#item-vehicle-container').slimScroll({
        start: 'bottom',
        height: '',
        railVisible: true,
    });
    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();
    })

    $('#item-vechicle-clone').find('input[name="dueDate"]').datepicker('destroy');

    $('#btn-create-transport-order').on('click', function() {
        if (!$('#transport-arrangement-form').find('.valid-required-transport-fields').valid()) {
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
            //            let isChecked = $(this).find('input[name="inspectionFlag"]').is(":checked");
            //            if (isChecked) {
            //                object.inspectionFlag = 1
            //            } else {
            //                object.inspectionFlag = 0
            //            }
            object.pickupDate = scheduleDetails.pickupDate;
            object.pickupTime = scheduleDetails.pickupTime;
            object.deliveryDate = scheduleDetails.deliveryDate;
            object.deliveryTime = scheduleDetails.deliveryTime;
            object.scheduleType = selectedType.selectedtype;
            object.selectedDate = scheduleDetails.selecteddate;
            object.comment = transportComment.comment;
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
                    $.redirect(myContextPath + '/transport/list', '', 'GET');
                    localStorage.setItem("aaj-shipping-dashboard-active-nav", '2');

                }

            }
        });
    })

    $('#table-purchaseConfirmed').on('click', '#cancel-purchase', function() {
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
                $(alertEle).css('display', '').html('<strong>Success!</strong> Stock Cancelled.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
                //set status
                setShippingDashboardStatus();
            }
        });
    })
    $('#table-purchaseConfirmed').on('click', '#edit-purchase', function() {
        var stockNo;
        var rowData = table.row($(this).closest('tr')).data();
        stockNo = rowData.stockNo;
        if (!isEmpty(stockNo)) {
            $.redirect(myContextPath + '/stock/stock-entry/' + stockNo, {}, "GET");
        }

    })

    //stock details modal update
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: '100%'
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
        //        if (isEmpty(response.data) || response.data.length == 0) {
        //            result = transportersJson;
        //        } else {
        //            result = response.data;
        //            autopopulate = true;
        //        }
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
function isValidSelection(data) {
    var tmpPData = '';
    var tmpPSupplier = '';
    for (var i = 0; i < data.length; i++) {
        console.log(data[i].sPurchaseInfoDate)
        var tableData = data[i];
        if (i == 0) {
            tmpPData = tableData.sPurchaseInfoDate;
            tmpPSupplier = tableData.prchsInfoSupplier;
        } else if (!(tmpPData == tableData.sPurchaseInfoDate && tmpPSupplier == tableData.prchsInfoSupplier)) {
            return false;
        }

    }
    return true;
}
function isCountSelection(data) {
    var tmpPCount = '';
    for (var i = 0; i < data.length; i++) {
        var tableData = data[i];
        if (i == 0) {
            tmpPCount = tableData.transportationCount;
        } else if (!(tmpPCount == tableData.transportationCount)) {
            return false;
        }

    }
    return true;
}
function isCompleteCountSelection(data) {
    for (var i = 0; i < data.length; i++) {
        if (data.transportationCount > 1) {
            return true;
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
function isPickupAndDropIsSame(element) {
    var container = $(element).closest('.item-vehicle')
    var from = container.find('select[name="pickupLocation"]').val();
    var to = container.find('select[name="dropLocation"]').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}
