var categoryJson, makersJson, transmissionTypesJson, fuelTypesJson, supplierJson, locationJson, salesName;
var id = ifNotValid($('#id').val(), '');
$(function() {
    //prevent form to submit on enter
    $(window).keydown(function(event) {
        if (event.keyCode == 13) {
            event.preventDefault();
            return false;
        }
    });
    var pathName = window.location.pathname.split('/');
    var searchQuery = window.location.search.split('/');
    var names = ['OLS','STK'];
    if ((!isEmpty(pathName[4])) && (names.indexOf(pathName[4].match('^[A-Z]{3}')) == -1)) {
        $('#stockEntryForm').find('#btn-stock-history').removeClass('hidden');
        $('#stockEntryForm').find('#purchaseInfoContainer').find('input.purchase-info-calc').prop('readonly', true);
        if (searchQuery.length > 0 && searchQuery[2] == 'documents') {
            $('#stockEntryForm').find('#chassisNo').attr('readonly', false);
        } else if ($('#stockEntryForm').find('#status').attr('data-value') == 0) {
            $('#stockEntryForm').find('#chassisNo').attr('readonly', false);
        } else {
            $('#stockEntryForm').find('#chassisNo').attr('readonly', true);
        }

    } else {
        $('#stockEntryForm').find('#btn-stock-history').addClass('hidden');
    }
    $('input[type="radio"][name="stock.isBidding"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    var radiobtn = $('input[name="stock.isBidding"]#bid').attr('data-value');
    if (radiobtn == 1) {
        $('input[type="radio"][name="stock.isBidding"]#bid').iCheck('check').trigger('ifChecked');
    } else {
        $('input[type="radio"][name="stock.isBidding"]#stk').iCheck('check').trigger('ifChecked');

    }

    $(".year-month-picker").inputmask({
        mask: "9999/99"
    });

    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('#transportInfo').on('change', '.select2.with-others', function() {
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.others-input-container').removeClass('hidden');
            $(closest_container).find('.select2').addClass('hidden');
        }
    }).on('click', 'a.show-dropdown', function() {
        var closest_container = $(this).closest('.form-group');
        $(closest_container).find('select.select2').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('.select2').removeClass('hidden');
        $(closest_container).find('div.others-input-container').addClass('hidden');

    });
    // Transmission Type Manual check
    $('#transmission').on('change', function() {
        var transVal = $(this).val();
        if (transVal == "MANUAL") {
            $('#manualTypes').prop("disabled", false);
        } else {
            $('#manualTypes').prop("disabled", true);
            $('#manualTypes').val('').trigger('change');

        }
    })

    $('#stockNo').select2({
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

    /*numbers*/
    $('.autonumber').autoNumeric('init');
    //create unique id of temporary attachment directory in serverside
    $('#attaachment_directory').val(getuuid());

    $('#maker').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#model');
        $(modelEle).empty();
        $('#subcategory').empty();
        $('#extraEquipments').empty();
        var transporter = $('#transportInfo').find('#transporter');
        $('#category').val('');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelId,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");

        }

    });
    $('#subcategory').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        placeholder: "Select Sub Category",

    })
    $('#extraEquipments').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true
    }).on('change', function() {
        var valueData = $(this).val();
        $.each(valueData, function(index, element) {
            if (element == "TYRE SIZE") {
                $('#tyreSizeDiv').removeClass('hidden');
            }
            if (element == "CRANE TYPE") {
                $('#craneTypeDiv').removeClass('hidden');
            }
            if (element == "CRANE CUT ( DAN )") {
                $('#craneCutDiv').removeClass('hidden');
            }
            if (element == "EXEL") {
                $('#exelDiv').removeClass('hidden');
            }
            if (element == "FUEL TANK KILO LITRE") {
                $('#tankKiloLitreDiv').removeClass('hidden');
            }

        })
    })
    $('#model').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    }).on('change', function() {
        var data = $(this).select2('data');
        var categorieEle = $('#category');
        var subcategorieEle = $('#subcategory');
        let extraEquipmentsEle = $('#extraEquipments');
        subcategorieEle.empty();

        if (data.length > 0 && !isEmpty(data[0].data)) {
            var catg = ifNotValid(data[0].data.category, '');
            $.getJSON(myContextPath + "/data/categories.json", function(data) {
                categoryJSON = data;
                categorieEle.attr('value', catg);
                if (catg == "HEAVY TRUCKS" || catg == "NORMAL TRUCKS") {
                    $('#extraEquipmentsDiv').removeClass('hidden');
                } else {
                    $('#extraEquipmentsDiv').addClass('hidden')
                }
                var subcatg = [];
                var extraEquips = [];
                if (!isEmpty(categoryJSON)) {
                    for (let i = 0; i < categoryJSON.length; i++) {
                        if (catg == categoryJSON[i].name) {
                            subcatg = categoryJSON[i].subCategories
                        }
                        if (catg == categoryJSON[i].name) {
                            extraEquips = categoryJSON[i].extraEquipments
                        }
                    }

                    $(subcategorieEle).select2({
                        matcher: function(params, data) {
                            return matchStart(params, data);
                        },
                        allowClear: true,
                        data: $.map(subcatg, function(item) {
                            return {
                                id: item.name,
                                text: item.name
                            };
                        })
                    }).val($(subcategorieEle).attr('value')).trigger("change");

                    $.getJSON(myContextPath + "/data/extra-equipments.json", function(data) {
                        extraEquips = data;
                        extraEquipmentsEle.select2({
                            allowClear: true,
                            width: '100%',
                            data: $.map(extraEquips, function(item) {
                                return {
                                    id: item.name,
                                    text: item.name
                                };
                            })
                        }).val(!isEmpty($(extraEquipmentsEle).attr('data-value')) ? $(extraEquipmentsEle).attr('data-value') : "").trigger('change')
                    })
                }
            })

        }
        //         transporter.select2({
        //             allowClear: true
        //         }).val('').trigger('change')
    });
    $('#photos_preview').on('click', 'span[name="img_delete"]', function() {
        $(this).closest('.img-wrap').remove();
    })
    $('#attachment-block').on('click', '.file-remove', function() {
        var result = confirm("Want to delete?");
        if (result) {
            $(this).find('i').removeClass('fa-fw fa-trash-o');
            $(this).find('i').addClass('fa-spinner fa-spin');
            deleteSingleFile(this)
        }
    });
    $('#btn-search-stock').click(function() {
        var stockNo = $('#stockNo').val();
        if (!isEmpty(stockNo)) {
            $.redirect(myContextPath + '/stock/stock-entry/' + stockNo, {}, "GET");
        }

    })
    $('#modelType').on('change', function() {
        $.ajax({
            beforeSend: function() {
                $('#spinner').show();
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/stockModelType/details?modelType=" + $(this).val(),
            contentType: "application/json",

            success: function(data) {
                if (data.status == 'success') {
                    data = data.data;
                    if (!isEmpty(data)) {
                        $('#maker').val(data.maker).trigger('change');
                        $('#subcategory').attr('value', data.subcategory);
                        $('#model').val(data.model).trigger('change');
                        $('#category').attr('value', data.category);
                        $('#transmission').val(data.transmission).trigger('change');
                        $('#manualTypes').val(data.manualTypes).trigger('change');
                        // setAutonumericValue($('#noOfDoors'), data.noOfDoors);
                        //$('#noOfSeat').attr('value', data.noOfSeat);
                        $('#fuel').val(data.fuel).trigger('change');
                        $('#driven').val(data.driven).trigger('change');
                        setAutonumericValue($('#cc'), data.cc);
                        $('#unit').val(data.unit).trigger('change');
                        $.each(data.equipment, function(index, value) {
                            $.each($('input[name="stock.equipment"]'), function(equipIndex, equipEle) {
                                if (value == $(equipEle).val()) {
                                    $(equipEle).attr('checked', true);
                                }
                            })
                        })
                    }
                }

            }
        });
    })
    var purchaseInfoContainer = $('#purchaseInfoContainer');
    purchaseInfoContainer.on('keyup', ".purchase-info-calc", function() {
        var purchaseCostEle = $('#purchaseCost');
        var purchaseCostTaxEle = $('#purchaseCostTax');
        var recycleAmountEle = $('#recycleAmount');
        var commisionAmountEle = $('#commisionAmount');
        var commisionTaxEle = $('#commisionTax');
        var otherChargesEle = $('#otherCharges');
        var roadTaxEle = $('#roadTax');
        //purchase price
        var purchasePrice = Number(purchaseCostEle.autoNumeric('init').autoNumeric('get'));
        var purchaseCostTaxPercent = Number(purchaseCostTaxEle.autoNumeric('init').autoNumeric('get'));
        var purchaseCostTax = (purchasePrice * purchaseCostTaxPercent) / 100;
        //recycle price
        var recycleAmount = Number(recycleAmountEle.autoNumeric('init').autoNumeric('get'));
        //commission price
        var commisionAmount = Number(commisionAmountEle.autoNumeric('init').autoNumeric('get'));
        var commisionTaxPercent = Number(commisionTaxEle.autoNumeric('init').autoNumeric('get'));
        var commisionTax = (commisionAmount * commisionTaxPercent) / 100;
        //Other Charges
        var otherCharges = Number(otherChargesEle.autoNumeric('init').autoNumeric('get'));

        var roadTaxAmount = Number(roadTaxEle.autoNumeric('init').autoNumeric('get'));
        var total = purchasePrice + recycleAmount + commisionAmount + otherCharges + roadTaxAmount;
        var totalTax = purchaseCostTax + commisionTax;
        var totalTaxIncluded = total + totalTax;
        //set total amount     

        $('#total').autoNumeric('init').autoNumeric('set', total);
        $('#totalTaxAutonumber').autoNumeric('init').autoNumeric('set', totalTax);
        //set total with tax amount
        $('#totalTaxIncludedAutonumber').autoNumeric('init').autoNumeric('set', totalTaxIncluded);

    })

    $('#purchasedSupplier').on("change", function(event) {

        var purchasedType = $('#purchaseType').find('option:selected').attr('data-type');
        if (isEmpty($(this).val()) | $('#purchaseType :selected').val() != 'auction') {
            $('#purchaseInfoPos, #purchasedAuctionHouse').empty();
            return;
        }

        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var auctionHouseArr = data[0].data.supplierLocations;
            $('#purchasedAuctionHouse').empty().select2({
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

    });
    $('#purchaseInfoPos').select2({
        placeholder: "Select POS",
        allowClear: true
    });
    //Auction house select2
    $('#purchasedAuctionHouse').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true
    }).on("change", function(event) {
        var auctionHouse = $(this).find('option:selected').val();

        if ((auctionHouse == null || auctionHouse.length == 0)) {
            $('#purchaseInfoPos').empty();
            return;
        }
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var posNos = data[0].data.posNos;
            $('#purchaseInfoPos').empty().select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                placeholder: "Select POS NO",
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
    // iCheck for checkbox and radio inputs
    $('input[type="radio"][name="stock.account"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $('input[type="radio"][name="stock.isMovable"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    $('#select_sales_staff').change(function() {
        salesName = $(this).find('option:selected').val();
    });

    var custName = $('#custId').attr('data-value');
    if (isEmpty(custName)) {
        $('#custId').removeClass('readonly select2-select');
        $('input[name="stock.isBidding"]').prop('disabled', false);
    } else {
        $('#custId').addClass('readonly select2-select');
        // $('input[name="stock.isBidding"]').prop('disabled',true);
    }

    var custInitId = $('#custId');
    if (!isEmpty(custName)) {

        $.ajax(myContextPath + "/customer/data/" + custName, {
            type: "get",
            contentType: "application/json",
            async: false
        }).done(function(data) {
            data = data.data
            custInitId.select2({
                "data": [{
                    id: data.code,
                    text: data.companyName + ' :: ' + data.firstName + ' ' + data.lastName + '(' + data.nickName + ')',
                    data: data
                }]
            }).val(data.code).trigger('change');
            initCustomerSelect2(custInitId);

        });

        //reserve customer server side search 
    } else {
        initCustomerSelect2(custInitId);
    }

    $('input[name="stock.isBidding"]').change(function() {
        if ($('input[name="stock.isBidding"]:checked').val() == 1) {
            $('#ifBidding').css('display', '');
            initCustomerSelect2(custInitId);
        } else {
            $('#ifBidding').css('display', 'none');
        }
    });

    // Date picker
    $('.datepicker.default-today').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })
    var purchaseInfoDate = $('input[name="stock.purchaseInfo.date"]').attr('value');
    $('input[name="stock.purchaseInfo.date"]').datepicker("setDate", ifNotValid(purchaseInfoDate, '').length > 0 ? ifNotValid(purchaseInfoDate, '') : 'now')
    //     
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
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                width: '100%',
                data: $.map(tmpSupplier, function(item) {
                    return {
                        id: item.supplierCode,
                        text: item.company,
                        data: item
                    };
                })
            })
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
        }
        $(purchasedSupplierEle).val('').trigger('change');
    });

    $('#numberPlate').change(function() {
        var numbPlt = $(this).find('option:selected').val()
        setNumberPlate(numbPlt)
        setRecycle(numbPlt);
    });
    $('#numberPlate').val($('#numberPlate').attr('data-value')).trigger('change');

    $('#stockEntryForm').on('submit', function(e) {

        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) {
            e.preventDefault();
            return false;
        }

        if ($(this).valid()) {

            var autoNumericElements = $('.autonumber');
            autoNumericSetRawValue(autoNumericElements)
        }
    })

    //alert on save , save and continue
    $('#save,#saveAndContinue').on('click', function(event) {
        //         var isExist = checkChassisNo($('#chassisNo').val());
        //         if (isExist) {
        //             if (!confirm($.i18n.prop('common.alert.chassisNo.check.exist'))) {
        //                 return false;
        //             }

        //         } else
        if (!confirm('Do you want to save?')) {
            return false;
        }

    })
    var transporterElement = $('#transporter')

    $('#transportInfo').on('change', '#transportPickupLoc', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this);
        var dropLocationEle = $('#transportInfo').find('#transportDropupLoc');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $('#transportInfo').find('#transporter');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty();
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData) && autopopulate) {
            toSelectVal = transporterData;
        } else {
            toSelectVal = transporter.find('option:first').val();
        }
        transporter.val(!isEmpty($(transporter).attr('data-value')) ? $(transporter).attr('data-value') : toSelectVal).trigger('change');

    })
    $('#transportInfo').on('change', '#transportDropupLoc', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $('#transportInfo').find('#transportPickupLoc');
        var dropLocationEle = $(this);
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $('#transportInfo').find('#transporter');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty();
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData) && autopopulate) {
            toSelectVal = transporterData;
        } else {
            toSelectVal = transporter.find('option:first').val();
        }
        transporter.val(!isEmpty($(transporter).attr('data-value')) ? $(transporter).attr('data-value') : toSelectVal).trigger('change');
    })
    $('#transportInfo').on('change', '#transporter', function() {
        var charge = $('#transportInfo').find('#charge');
        var valData = $(this).find('option:selected').data('data');
        if (!isEmpty(valData)) {
            if (!isEmpty(valData.data)) {
                $(charge).autoNumeric('init').autoNumeric('set', ifNotValid(valData.data.amount, 0));
            }
        } else {
            $(charge).autoNumeric('init').autoNumeric('set', 0);

        }
    });
    var destinationPortElement = $('#destinationPort');
    destinationPortElement.select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        width: '100%',
    })
    $('#destinationCountry').on('change', function() {
        destinationPortElement.empty();
        var data = $(this).find('option:selected').data('data');
        if (!isEmpty(data && data.data)) {
            $('#destinationPort').select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                width: '100%',
                data: $.map(data.data.port, function(item) {
                    return {
                        id: item,
                        text: item

                    };
                })
            })
            $('#destinationPort').val($('#destinationPort').attr('data-value')).trigger("change");
        }

    })
    $('#orgin').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        width: '100%'
    })
    $.ajaxSetup({
        async: false
    });

    $.getJSON(myContextPath + "/data/transmissionTypes.json", function(data) {
        transmissionTypesJson = data;
        var ele = $('#transmission');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(transmissionTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    // Master Auction Grades Find All
    $.getJSON(myContextPath + "/data/auctionInteriorGrades.json", function(data) {
        auctionGradesIntJson = data;
        var ele = $('#auctionGrade');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(auctionGradesIntJson, function(item) {
                return {
                    id: item.grade,
                    text: item.grade
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/auctionExteriorGrades.json", function(data) {
        auctionGradesExtJson = data;
        var ele = $('#auctionGradeExt');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(auctionGradesExtJson, function(item) {
                return {
                    id: item.grade,
                    text: item.grade
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/colors.json", function(data) {
        colorsJson = data;
        var ele = $('#colors');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(colorsJson, function(item) {
                return {
                    id: item.color,
                    text: item.color
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#maker');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
        $('#model').val($('#model').attr('data-value')).trigger("change");
        $('select#subcategory').val($('select#subcategory').attr('data-value')).trigger("change");
        $('#category').val($('#category').attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#transporter').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
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
        $('#transporter').val($('#transporter').attr('data-value')).trigger("change");

    })
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('.locations').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        })
        var newOption = new Option("Others","others",false,false);
        // Append it to the select
        $('.locations').append(newOption).trigger('change');
        $('#transportPickupLoc').val($('#transportPickupLoc').attr('data-value')).trigger("change");
        $('#transportDropupLoc').val($('#transportDropupLoc').attr('data-value')).trigger("change");
        $('#destinationCountry').val($('#destinationCountry').attr('data-value')).trigger("change");
        $('#transporter').val($('#transporter').attr('data-value')).trigger("change");

    })
    // to disable async

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        var countriesJson = data;
        $('#destinationCountry').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        })
        $('#destinationCountry').val($('#destinationCountry').attr('data-value')).trigger("change");
        $('#orgin').val($('#orgin').attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/fuelTypes.json", function(data) {
        fuelTypesJson = data;
        var ele = $('#fuel');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(fuelTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type,
                    data: item
                };
            })
        })
        $('#fuel').val($('#fuel').attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/active/suppliers.json", function(data) {
        supplierJson = data;
        var supplierEle = $('#purchasedSupplier');
        var selectedVal = $('#purchaseType').find('option:selected').attr('data-type');
        var tmpSupplier = [];
        if (selectedVal) {
            tmpSupplier = supplierJson.filter(function(item) {
                return item.type === selectedVal;
            })
            if (selectedVal == 'auction') {
                $('#auctionFields').css('display', '')
            } else {
                $('#auctionFields').css('display', 'none')
            }
            $(supplierEle).prop("disabled", false);
        }

        $(supplierEle).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(tmpSupplier, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).val($(supplierEle).attr('data-value')).trigger("change")
        var auctionHouseEle = $('#purchasedAuctionHouse');
        var purchaseInfoPosEle = $('#purchaseInfoPos');
        $(auctionHouseEle).val($(auctionHouseEle).attr('data-value')).trigger("change");
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");

    });
    $.getJSON(myContextPath + "/user/getRoleSales", function(data) {
        var salesJson = data;
        var ele = $('#select_sales_staff');
        var selectedVal = $('#select_sales_staff').find('option:selected').val();
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                var staff = item.username + +(item.userId)
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
        isEmpty(custName) ? ele.removeClass('readonly select2-select') : ele.addClass('readonly select2-select');
    })
    $.getJSON(myContextPath + "/data/crane-cut.json", function(data) {
        craneCutJson = data;
        var ele = $('#craneCut');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(craneCutJson, function(item) {
                return {
                    id: item.craneCut,
                    text: item.craneCut
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/crane-type.json", function(data) {
        craneTypeJson = data;
        var ele = $('#craneType');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(craneTypeJson, function(item) {
                return {
                    id: item.craneType,
                    text: item.craneType
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/exel.json", function(data) {
        exelJson = data;
        var ele = $('#exel');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(exelJson, function(item) {
                return {
                    id: item.exel,
                    text: item.exel
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/tankKiloLitre.json", function(data) {
        tankKiloLitreJson = data;
        var ele = $('#tankKiloLitre');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(tankKiloLitreJson, function(item) {
                return {
                    id: item.tankKiloLitre,
                    text: item.tankKiloLitre
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    $.getJSON(myContextPath + "/data/tyre-size.json", function(data) {
        tyreSizeJson = data;
        var ele = $('#tyreSize');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(tyreSizeJson, function(item) {
                return {
                    id: item.tyreSize,
                    text: item.tyreSize
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwarderJson = data;
        var ele = $('#forwarder');
        $(ele).select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/extra-equipments.json", function(data) {
        extraEquips = data;

        var extraEquipmentsEle = $('#extraEquipments');

        let testEquips = [];
        //data-value="[TEST4, TEST5, TEST6]"
        let obj = !isEmpty($(extraEquipmentsEle).attr('data-value')) ? $(extraEquipmentsEle).attr('data-value') : ""
        let rem1 = obj.replace("[", "");
        let rem2 = rem1.replace("]", "");
        var res = rem2.split(", ");
        $.each(res, function(index, element) {
            testEquips.push(element)
        })
        extraEquipmentsEle.select2({
            allowClear: true,
            width: '100%',
            data: $.map(extraEquips, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        }).val(testEquips).trigger('change')
    })

    // to enable async
    $.ajaxSetup({
        async: true
    });

    //PickUp Location Auto set
    $('#purchasedAuctionHouse').on('change', function() {
        // ('transportPickupLoc')
        var purchasedSupplier = $('#purchasedSupplier').find('option:selected').val();
        var purchasedAuctionHouse = $('#purchasedAuctionHouse').find('option:selected').val();
        if (purchasedSupplier != undefined && purchasedAuctionHouse != undefined) {
            var result = $.grep(locationJson, function(e) {
                return e.supplierCode == purchasedSupplier;
            });
            var result1 = $.grep(result, function(e) {
                return e.sAuctionHouseId == purchasedAuctionHouse;
            });
            if (result1.length > 0) {
                $('#transportPickupLoc').val(result1[0].code).trigger("change");
            }
        }

    })
    $('select#select_sales_staff').prop('tabindex', 1);
    $('select#custId').prop('tabindex', 1);
    //to set default values
    if (isEmpty(id)) {
        setDefaults();
    } else {
        $('#charge').prop('readonly', true);
    }
})
function setRecycle(recycleVal) {
    switch (recycleVal) {
    case "no":

        $('#roadTax').val('').attr('disabled', 'disabled');
        $('#purchaseInfoContainer').find('#roadTaxText.autonumber').trigger('keyup');
        break;
    case "yes":
        $('#recycle').val(recycleVal).prop('selected', true)
        $('#roadTax').removeAttr('disabled');
        break;
    case undefined:
        $('#roadTaxText').val('').prop('readonly', true);
        $('#purchaseInfoContainer').find('#roadTaxText.autonumber').trigger('keyup');
        break;
    }
}
function setNumberPlate(numbVal) {
    //.trigger('change');//$(this).attr('data-value')
    if (numbVal == "no") {
        $('#numberPlate').val(numbVal).prop('selected', true)
        $('#oldNp').val('').prop('readonly', true);
    } else if (numbVal == "yes") {
        $('#numberPlate').val(numbVal).prop('selected', true)
        $('#oldNp').prop('readonly', false);
    } else {
        $('#oldNp').val('').prop('readonly', true);
    }
}
function setDefaults() {
    $('#transmission').val('AUTOMATIC').trigger("change");
    $('#fuel').val('Gasoline').trigger("change");
    $('#orgin').val('JAPAN').trigger("change");
    $('#driven').val('Right');
    $('#purchaseCostTax').autoNumeric('set', $.i18n.prop('common.tax'));
    $('#commisionTax').autoNumeric('set', $.i18n.prop('common.tax'));
    $('#recycle').val('yes').trigger('change');
    $('#numberPlate').val('no').trigger('change');
    $('#shipmentType').val('1').trigger('change');
    $('#unit').val('cc').trigger("change");
}
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
    var closest_container = $('#transportInfo');
    var category = $('#subcategory').val();
    var from = closest_container.find('#transportPickupLoc').val()
    var to = closest_container.find('#transportDropupLoc').val()
    var queryString = "?category=" + category + "&from=" + from + "&to=" + to
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
                return $('#_stockNo').val()
            },
            chassisNo: function() {
                return $('#chassisNo').val()
            }
        },
        success: function(data) {
            response = data;
        }

    })
    return response;

}
function isPickupAndDropIsSame(element) {
    var container = $(element)
    var from = $('#transportInfo').find('#transportPickupLoc').val();
    var to = $('#transportInfo').find('#transportDropupLoc').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}
function initCustomerSelect2(element) {
    $(element).select2({

        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/shipping/search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public',
                    salesName: salesName
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

    })
}
