var locationJson, forwardersJson, categoriesJson, transportersJson, countryJson, suppliersJson, table;
let transportItemCount = 0;
$(function() {
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    //set status
    setShippingDashboardStatus();
    //AutoNumeric
    //     $('input[name="charge"]').autoNumeric('init');
    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        $('#modal-arrange-transport,#modal-edit-transport').find('select[name="destinationCountry"]').select2({
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
        $('#modal-arrange-transport #item-vechicle-clone,#modal-edit-transport #item-vechicle-clone').find('select[name="destinationCountry"]').select2('destroy')
    })

    //     function showDetails(tr) {
    //         // var tr = $(this).closest('tr');
    //         var row = table.row(tr)
    //         table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
    //             var row = table.row(rowIdx);
    //             if (row.child.isShown()) {
    //                 row.child.hide();
    //                 tr.removeClass('shown');
    //             }
    //         })
    //         //row.child(format(row.data())).show();
    //         var detailsElement = format(row.data());
    //         row.child(detailsElement).show();
    //         detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
    //         tr.addClass('shown');
    //         tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    //     }
    //     function showConfirmDetails(row) {
    //         if (!row.child.isShown()) {
    //             var tr = $(row.node()).closest('tr');
    //             var detailsElement = format(row.data());
    //             row.child(detailsElement).show();
    //             detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
    //             tr.addClass('shown');
    //             tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    //             return true;
    //         } else {
    //             return false;
    //         }

    //     }
    //     function showCompletedDetails(tr) {
    //         // var tr = $(this).closest('tr');
    //         var row = table_completed.row(tr)
    //         table_completed.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
    //             var row = table_completed.row(rowIdx);
    //             if (row.child.isShown()) {
    //                 row.child.hide();
    //                 tr.removeClass('shown');
    //             }
    //         })
    //         //row.child(format(row.data())).show();
    //         var detailsElement = format(row.data());
    //         row.child(detailsElement).show();
    //         detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
    //         tr.addClass('shown');
    //         tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    //     }
    let lastDrawDataIndex = 0;
    let transportChassisNoMatchIndexArr = [];
    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {

        if (settings.sTableId == 'table-transport') {
            var term = $('#table-filter-search').val().toLowerCase();
            var chassisNoSearchTerm = $('#table-filter-search-chassisNo').val().toLowerCase();
            var supplierFilterName = $('select#purchasedSupplier').find('option:selected').val();
            var auctionHouseFilter = $('select#purchasedAuctionHouse').find('option:selected').val();
            var orderItem = JSON.parse(data[5]);
            let highlightArr = [];
            var row = table.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            if (isEmpty(term) && isEmpty(chassisNoSearchTerm) && isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                return true
            }

            let isFound = false;
            for (var i = 0; i < orderItem.length; i++) {
                if ((supplierFilterName == null || supplierFilterName.length == 0)) {
                    $('#purchasedAuctionHouse').empty();
                }

                if (!isEmpty(term)) {

                    if (ifNotValid(orderItem[i]["model"], '').toLowerCase().indexOf(term) != -1)
                        return true;
                    if (ifNotValid(orderItem[i]["sPickupLocation"], '').toLowerCase().indexOf(term) != -1)
                        return true;
                    if (ifNotValid(orderItem[i]["sDropLocation"], '').toLowerCase().indexOf(term) != -1)
                        return true;
                    if (ifNotValid(orderItem[i]["lotNo"], '').toLowerCase().indexOf(term) != -1)
                        return true;
                    if (ifNotValid(orderItem[i]["finalDestination"], '').toLowerCase().indexOf(term) != -1)
                        return true;
                } else if (!isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                    if (ifNotValid(orderItem[i]["supplierCode"], '').indexOf(supplierFilterName) != -1)
                        return true;
                } else if (!isEmpty(supplierFilterName) && !isEmpty(auctionHouseFilter)) {
                    if (ifNotValid(orderItem[i]["auctionHouseId"], '').indexOf(auctionHouseFilter) != -1)
                        return true;
                } else if (!isEmpty(chassisNoSearchTerm)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(chassisNoSearchTerm) != -1) {
                        highlightArr.push(orderItem[i].invoiceId);
                        isFound = true;
                    }
                }

            }
            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                transportChassisNoMatchIndexArr.push(object);
                return true
            }
            return false;
        } else if (settings.sTableId == 'table-confirm-transport') {

            var term = $('#table-confirm-filter-search').val().toLowerCase();
            var chassisNoSearchTerm = $('#table-confirm-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[4]);
            var supplierFilterName = $('select#supplierConfirm').find('option:selected').val();
            var auctionHouseFilter = $('select#auctionHouseConfirm').find('option:selected').val();
            let detailsViewShown = false;
            let detailsTRElement;
            let highlightArr = [];
            let isFound = false;
            var row = table_confirm.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            if (isEmpty(term) && isEmpty(chassisNoSearchTerm) && isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                return true
            }
            for (var i = 0; i < orderItem.length; i++) {
                if ((supplierFilterName == null || supplierFilterName.length == 0)) {
                    $('#auctionHouseConfirm').empty();

                }
                if (!isEmpty(term)) {

                    if (ifNotValid(orderItem[i]["model"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }
                    if (ifNotValid(orderItem[i]["sPickupLocation"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }
                    if (ifNotValid(orderItem[i]["sDropLocation"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }
                    if (ifNotValid(orderItem[i]["lotNo"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }
                    if (ifNotValid(orderItem[i]["finalDestination"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }

                } else if (!isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                    if (ifNotValid(orderItem[i]["supplierCode"], '').indexOf(supplierFilterName) != -1)
                        return true;
                } else if (!isEmpty(supplierFilterName) && !isEmpty(auctionHouseFilter)) {
                    if (ifNotValid(orderItem[i]["auctionHouseId"], '').indexOf(auctionHouseFilter) != -1)
                        return true;
                } else if (!isEmpty(chassisNoSearchTerm)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(chassisNoSearchTerm) != -1) {
                        highlightArr.push(orderItem[i].invoiceId);
                        isFound = true;
                    }
                }

            }

            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                transportChassisNoMatchIndexArr.push(object);
                return true
            }
            return false;

        } else if (settings.sTableId == 'table-completed-transport') {
            var term = $('#table-completed-filter-search').val().toLowerCase();
            var chassisNoSearchTerm = $('#table-completed-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[4]);
            var supplierFilterName = $('select#supplierCompleted').find('option:selected').val();
            var auctionHouseFilter = $('select#auctionHouseCompleted').find('option:selected').val();
            let isFound = false;
            let highlightArr = [];
            var row = table_completed.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            if (isEmpty(term) && isEmpty(chassisNoSearchTerm) && isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                return true
            }
            for (var i = 0; i < orderItem.length; i++) {
                if ((supplierFilterName == null || supplierFilterName.length == 0)) {
                    $('#auctionHouseCompleted').empty();
                }
                if (!isEmpty(term)) {

                    if (~ifNotValid(orderItem[i]["model"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(orderItem[i]["sPickupLocation"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(orderItem[i]["sDropLocation"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(orderItem[i]["lotNo"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(orderItem[i]["finalDestination"], '').toLowerCase().indexOf(term))
                        return true;
                } else if (!isEmpty(supplierFilterName) && isEmpty(auctionHouseFilter)) {
                    if (~ifNotValid(orderItem[i]["supplierCode"], '').indexOf(supplierFilterName))
                        return true;
                } else if (!isEmpty(supplierFilterName) && !isEmpty(auctionHouseFilter)) {
                    if (~ifNotValid(orderItem[i]["auctionHouseId"], '').indexOf(auctionHouseFilter))
                        return true;

                } else if (!isEmpty(chassisNoSearchTerm)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(chassisNoSearchTerm) != -1) {
                        highlightArr.push(orderItem[i].invoiceId);
                        isFound = true;
                    }
                }

            }
            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                transportChassisNoMatchIndexArr.push(object);
                return true
            }
            return false;
        }

        return true;
    })
    //Transporter Select & Status Select Filter
    var filterTransporterSelect = $('#transporterSelect').find('option:selected').val();
    var filterTransporterSelectConfirm = $('#transporterSelectConfirm').find('option:selected').val();
    var filterTransporterSelectCompleted = $('#transporterSelectCompleted').find('option:selected').val();
    let drawCount = 0;
    let maxIndex = 0;

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex, rowData, i, j) {

        if (oSettings.sTableId == 'table-transport') {
            var purchased_date = $('#table-filter-purchased-date').val();
            var created_date = $('#table-filter-created-date').val();

            if (typeof filterTransporterSelect != 'undefined' && filterTransporterSelect.length != '') {
                if (aData[2].length == 0 || aData[2] != filterTransporterSelect) {
                    return false;
                }
            }
            if (typeof created_date != 'undefined' && created_date.length != '') {
                if (aData[6].length == 0 || aData[6] != created_date) {
                    return false;
                }
            }
            if (typeof purchased_date != 'undefined' && purchased_date.length != '') {
                if (aData[4].length == 0 || aData[4] != purchased_date) {
                    return false;
                }
            }
            if (drawCount != oSettings.iDraw || (drawCount == oSettings.iDraw && lastDrawDataIndex == iDataIndex)) {
                drawCount = oSettings.iDraw;
                lastDrawDataIndex = iDataIndex;
                transportItemCount = 0;
            }

            transportItemCount += rowData.orderItem.length;

        } else if (oSettings.sTableId == 'table-confirm-transport') {
            var purchased_date = $('#table-confirm-filter-purchased-date').val();
            var created_date = $('#table-confirm-filter-created-date').val();

            if (typeof filterTransporterSelectConfirm != 'undefined' && filterTransporterSelectConfirm.length != '') {
                if (aData[1].length == 0 || aData[1] != filterTransporterSelectConfirm) {
                    return false;
                }
            }
            if (typeof created_date != 'undefined' && created_date.length != '') {
                if (aData[5].length == 0 || aData[5] != created_date) {
                    return false;
                }
            }
            if (typeof purchased_date != 'undefined' && purchased_date.length != '') {
                if (aData[3].length == 0 || aData[3] != purchased_date) {
                    return false;
                }
            }
            if (drawCount != oSettings.iDraw || (drawCount == oSettings.iDraw && lastDrawDataIndex == iDataIndex)) {
                drawCount = oSettings.iDraw;
                lastDrawDataIndex = iDataIndex;
                transportItemCount = 0;
            }

            transportItemCount += rowData.orderItem.length;
        } else if (oSettings.sTableId == 'table-completed-transport') {
            var purchased_date = $('#table-complete-filter-purchased-date').val();
            var created_date = $('#table-completed-filter-created-date').val();
            if (typeof filterTransporterSelectCompleted != 'undefined' && filterTransporterSelectCompleted.length != '') {
                if (aData[1].length == 0 || aData[1] != filterTransporterSelectCompleted) {
                    return false;
                }
            }
            if (typeof created_date != 'undefined' && created_date.length != '') {
                if (aData[5].length == 0 || aData[5] != created_date) {
                    return false;
                }
            }
            if (typeof purchased_date != 'undefined' && purchased_date.length != '') {
                if (aData[3].length == 0 || aData[3] != purchased_date) {
                    return false;
                }
            }
            if (drawCount != oSettings.iDraw || (drawCount == oSettings.iDraw && lastDrawDataIndex == iDataIndex)) {
                drawCount = oSettings.iDraw;
                lastDrawDataIndex = iDataIndex;
                transportItemCount = 0;
            }

            transportItemCount += rowData.orderItem.length;
        }

        return true;
    });

    $('#transporterSelect').select2({
        allowClear: true
    }).on("change", function(event) {
        filterTransporterSelect = $(this).find('option:selected').val();
        transportItemCount = 0;
        table.draw();
    });
    $('#transporterSelectConfirm').select2({
        allowClear: true
    }).on("change", function(event) {
        filterTransporterSelectConfirm = $(this).find('option:selected').val();
        transportItemCount = 0;
        table_confirm.draw();
    });
    $('#transporterSelectCompleted').select2({
        allowClear: true
    }).on("change", function(event) {
        filterTransporterSelectCompleted = $(this).find('option:selected').val();
        transportItemCount = 0;
        table_completed.draw();
    });
    $('#selectStatus').change(function() {
        filterStatusSelect = $('#selectStatus').val();
        table.draw();
    });

    //populate dropdown options
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#transport-items, #modal-arrange-transport #item-vechicle-clone, #modal-edit-transport #item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').select2({

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
        $('#transport-items, #modal-arrange-transport #item-vechicle-clone, #modal-edit-transport #item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').append(newOption);
        $('#modal-arrange-transport #item-vechicle-clone, #modal-edit-transport #item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').select2('destroy')
    })
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;
        $('#modal-arrange-transport #transport-items,#modal-edit-transport #transport-items').find('select[name="forwarder"]').select2({
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
        $('#modal-arrange-transport #transport-items,#modal-edit-transport #item-vechicle-clone').find('select[name="forwarder"]').select2('destroy')
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
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {

        $('#purchasedSupplier').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).on("change", function(event) {
            //             supplierFilterName = $(this).find('option:selected').val();

            $('#auctionFields').css('display', '')
            var data = $(this).select2('data');
            if (data.length > 0 && !isEmpty(data[0].data)) {
                var auctionHouseArr = data[0].data.supplierLocations;
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

            }
            table.draw();
        })
    });
    $('#purchasedAuctionHouse').on('change', function() {
        table.draw();
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {

        $('#supplierConfirm').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).on("change", function(event) {
            //             supplierFilterName = $(this).find('option:selected').val();

            $('#confirmAuctionFields').css('display', '')
            var data = $(this).select2('data');
            if (data.length > 0 && !isEmpty(data[0].data)) {
                var auctionHouseArr = data[0].data.supplierLocations;
                $('#auctionHouseConfirm').empty().select2({
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

            }
            table_confirm.draw();
        })
    });
    $('#auctionHouseConfirm').on('change', function() {
        table_confirm.draw();
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {

        $('#supplierCompleted').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).on("change", function(event) {
            //             supplierFilterName = $(this).find('option:selected').val();

            $('#completedAuctionFields').css('display', '')
            var data = $(this).select2('data');
            if (data.length > 0 && !isEmpty(data[0].data)) {
                var auctionHouseArr = data[0].data.supplierLocations;
                $('#auctionHouseCompleted').empty().select2({
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

            }
            table_completed.draw();
        })
    });
    $('#auctionHouseCompleted').on('change', function() {
        table_completed.draw();
    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#modal-arrange-transport,#modal-edit-transport').find('.transporter').select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        });
        $('#modal-arrange-transport #item-vechicle-clone, #modal-edit-transport #item-vechicle-clone').find('select[name="transporter"]').select2('destroy');
        $('#transporterSelect').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger("change");
    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#transporterSelectConfirm').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#transporterSelectCompleted,#transporterDeliveryConfirmed').select2({
            placeholder: "All",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        let element = $(this);

        if ($(element).val() == 0) {
            $('#transport-details-rearrange').addClass('hidden')
            $('#transport-delivery-confirmed').addClass('hidden')
            $('#transport-details').removeClass('hidden')
            $('#transport-confirm-details').addClass('hidden')
            $('#transport-completed').addClass('hidden')

            table.ajax.reload()
        } else if ($(element).val() == 1) {
            $('#transport-details').addClass('hidden')
            $('#transport-delivery-confirmed').addClass('hidden')
            $('#transport-details-rearrange').removeClass('hidden')
            $('#transport-confirm-details').addClass('hidden')
            $('#transport-completed').addClass('hidden')

            table_rearrange.ajax.reload()
        } else if ($(element).val() == 2) {
            $('#transport-details').addClass('hidden')
            $('#transport-details-rearrange').addClass('hidden')
            $('#transport-delivery-confirmed').addClass('hidden')
            $('#transport-confirm-details').removeClass('hidden')
            $('#transport-completed').addClass('hidden')

            table_confirm.ajax.reload()
        } else if ($(element).val() == 3) {
            $('#transport-details').addClass('hidden')
            $('#transport-details-rearrange').addClass('hidden')
            $('#transport-confirm-details').addClass('hidden')
            $('#transport-delivery-confirmed').addClass('hidden')
            $('#transport-completed').removeClass('hidden')

            table_completed.ajax.reload()
        } else if ($(element).val() == 4) {
            $('#transport-details').addClass('hidden')
            $('#transport-details-rearrange').addClass('hidden')
            $('#transport-confirm-details').addClass('hidden')
            $('#transport-delivery-confirmed').addClass('hidden')
            $('#transport-completed').removeClass('hidden')

            table_completed.ajax.reload()
        } else if ($(element).val() == 5) {
            $('#transport-details').addClass('hidden')
            $('#transport-details-rearrange').addClass('hidden')
            $('#transport-confirm-details').addClass('hidden')
            $('#transport-completed').addClass('hidden')
            $('#transport-delivery-confirmed').removeClass('hidden')

            tableDeliveryConfirmed.ajax.reload();
        }

    })

    var tableEle = $('#table-transport');
    var table = $(tableEle).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "autowidth": false,
        "ajax": {
            url: myContextPath + "/transport/list/datasource",
            beforeSend: function() {
                $('div#transport-details>.overlay').show()
            },
            complete: function() {
                $('div#transport-details>.overlay').hide();
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

            "data": "id",
            //             "visible":false,
            "render": function(data, type, row) {
                return '<input type="checkbox" id="select-box" />'
            }
        }, {
            targets: 1,
            "width": "90%",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var inspectionStatus;
                    var className;
                    var showAcceptBtn = false;
                    var transConfirmBtnShow = true;
                    var inspectionstatusArr = [];
                    var inspectionStatusData = {};
                    for (var i = 0; i < row.orderItem.length; i++) {

                        if (row.orderItem[i].status == 2) {
                            transConfirmBtnShow = false;
                        }
                        for (var j = 0; j < row.orderItem.length; j++) {
                            inspectionStatusData = row.orderItem[j].stockStatus
                            inspectionstatusArr.push(inspectionStatusData)
                        }
                        var count = 0;
                        var totalItems = inspectionstatusArr.length;
                        for (var k = 0; k < totalItems; k++) {
                            if (inspectionstatusArr[k] != 0) {
                                count++;
                            }
                            if (inspectionstatusArr.length == count) {
                                inspectionStatus = "Purchase confirmed";
                                className = "text-green"
                                //return
                            } else {
                                inspectionStatus = "Purchase not confirmed";
                                className = "text-red"
                            }
                        }
                        showAcceptBtn = true;
                        break;

                    }
                    var html = ''

                    // var customData = ifNotValid(row.pickupLocation, '').toUpperCase() == 'OTHERS' ? ifNotValid(row.pickupLocation, '') : ifNotValid(row.sPickupLocation, '');

                    html += '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" data-toggle="modal" data-target="#modal-edit-transport"><i class="fa fa-fw fa-bus"></i>Transport Edit</a>'

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=requested&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Order request [PDF]</a>'

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=requested&format=xls" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-excel-o"></i>Order request [Excel]</a>'

                    if (showAcceptBtn && transConfirmBtnShow) {
                        html += '<a href="#" class="ml-5 btn btn-primary btn-xs" name="btn-confirm-transport"><i class="fa fa-fw fa-check"></i>Confirm Transport</a>';
                    }

                    return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.transporterName + ' - ' + row.supplierName + ' - [' + row.purchaseDate + ']</span><span class="ml-5 ' + className + '">' + inspectionStatus + ' </span><span> - ' + totalItems + '</span></h5></div><div class="action-container pull-right">' + html + '</div></div></div></div>';
                }
                return data;
            }
        }, {
            targets: 2,
            "data": "transporterCode",
            "visible": false
        }, {
            targets: 3,
            "data": "transporterName",
            "visible": false
        }, {
            targets: 4,
            "data": "purchaseDate",
            "visible": false
        }, {
            targets: 5,
            data: 'orderItem',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }, {
            targets: 6,
            "data": "createdDate",
            "visible": false
        }],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
            $('span#transportItemCount').html(transportItemCount);
            transportChassisNoMatchIndexArr = getUnique(transportChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < transportChassisNoMatchIndexArr.length; i++) {
                var row = table.row(transportChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = transportChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            transportChassisNoMatchIndexArr = []
        }

    });
    $('button.btn-download-multiple-invoice').on('click', function() {
        let rows = table.rows('.selected').data().toArray();
        if (rows.length == 0) {
            alert('Please select atleast one invoice')
            return false;
        }
        let transporter = '';
        for (let i = 0; i < rows.length; i++) {
            if (i == 0) {
                transporter = rows[i].transporterCode;
                continue;
            }
            if (transporter != rows[i].transporterCode) {
                alert('Please select single transporter')
                return false;
            }
        }

        let invoiceIds = [];
        for (let i = 0; i < rows.length; i++) {
            for (let j = 0; j < rows[i].orderItem.length; j++) {
                invoiceIds.push(rows[i].orderItem[j].invoiceId);

            }
        }

        let params = {};
        params["invoiceIds"] = invoiceIds;
        params["format"] = $(this).attr('data-format');
        post_to_url(myContextPath + '/transport/multiple/order/request', params, 'post');

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

    });

    // Date range picker
    var purchased_date;
    $('#table-filter-purchased-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        purchased_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        //         table.search(purchased_date).draw();
        transportItemCount = 0;
        table.draw();

    });
    $('#date-form-group').on('click', '.clear-date', function() {

        purchased_date = '';
        $('#table-filter-purchased-date').val('');
        $(this).remove();
        transportItemCount = 0;
        table.draw();
    })

    var created_date;
    $('#table-filter-created-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        created_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.search(created_date).draw();
        //         table.draw();
    });
    $('#created-date-form-group').on('click', '.clear-date', function() {
        created_date = '';
        $('#table-filter-created-date').val('');
        table.search(created_date).draw();
        //         table.draw();
        $(this).remove();
    })

    var confirm_purchased_date;
    $('#table-confirm-filter-purchased-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        confirm_purchased_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transportItemCount = 0;
        table_confirm.search(confirm_purchased_date).draw();
    })
    $('#date-confirm-form-group').on('click', '.clear-date', function() {
        confirm_purchased_date = '';
        $('#table-confirm-filter-purchased-date').val('');
        transportItemCount = 0;
        table_confirm.search(confirm_purchased_date).draw();
        $(this).remove();
    })

    var confirm_created_date;
    $('#table-confirm-filter-created-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        confirm_created_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transportItemCount = 0;
        table_confirm.search(confirm_created_date).draw();
    });
    $('#confirm-created-date-form-group').on('click', '.clear-date', function() {
        confirm_created_date = '';
        $('#table-confirm-filter-created-date').val('');
        transportItemCount = 0;
        table_confirm.search(confirm_created_date).draw();
        $(this).remove();
    })

    var complete_purchased_date;
    $('#table-complete-filter-purchased-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        complete_purchased_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transportItemCount = 0;
        table_completed.search(complete_purchased_date).draw();
    });
    $('#date-complete-form-group').on('click', '.clear-date', function() {
        complete_purchased_date = '';
        $('#table-complete-filter-purchased-date').val('');
        transportItemCount = 0;
        table_completed.search(complete_purchased_date).draw();
        $(this).remove();
    })

    var complete_created_date;
    $('#table-completed-filter-created-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        complete_created_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transportItemCount = 0
        table_completed.search(complete_created_date).draw();
    });
    $('#completed-created-date-form-group').on('click', '.clear-date', function() {
        complete_created_date = '';
        $('#table-completed-filter-created-date').val('');
        transportItemCount = 0
        table_completed.search(complete_created_date).draw();
        $(this).remove();
    })

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        transportItemCount = 0;
        table.search($(this).val()).draw();

    });
    // Customize Datatable
    $('#table-filter-search-chassisNo').keyup(function(e) {
        transportItemCount = 0;
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            table.search($(this).val()).draw();
        }

    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    table.on('click', 'a[name="btn-confirm-transport"]', function() {
        var trow = $(this).closest('tr');
        var rowdata = table.row(trow);
        var datarow = rowdata.data();

        if (!confirm($.i18n.prop('common.confirm.transport'))) {
            return false;
        }

        var tr = $(this).closest('tr');
        var row = table.row(tr);
        var data = row.data();
        var invoiceNo = data.invoiceNo;
        var purchaseDate = data.purchaseDate;

        let params = data.orderItem.map(row=>row.invoiceId);

        //var queryString = "?invoiceNo=" + invoiceNo + "&purchaseDate=" + purchaseDate;
        var result = confirmTransport(params);
        if (result.status == 'success') {
            table.ajax.reload();
        }
    })
    table.on('click', 'td>.container-fluid>.row>.col-md-12>div.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    //confirm transport details
    var table_confirm_ele = $('#table-confirm-transport');
    var table_confirm = $(table_confirm_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        searchDelay: 5000,
        "pageLength": 25,
        "ordering": false,
        "autowidth": false,
        "ajax": {
            url: myContextPath + "/transport/confirm/list/datasource",
            beforeSend: function() {
                $('div#transport-confirm-details>.overlay').show()
            },
            complete: function() {
                $('div#transport-confirm-details>.overlay').hide();
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
            "width": "90%",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var showAcceptBtn = false;
                    for (var i = 0; i < row.orderItem.length; i++) {
                        if (row.orderItem[i].status != 1) {
                            continue;
                        }
                        showAcceptBtn = true;
                        break;
                    }
                    var totalItems = row.orderItem.length;
                    var html = ''

                    // var customData = ifNotValid(row.pickupLocation, '').toUpperCase() == 'OTHERS' ? ifNotValid(row.pickupLocation, '') : ifNotValid(row.sPickupLocation, '');

                    html += '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" data-toggle="modal" data-target="#modal-edit-transport"><i class="fa fa-fw fa-bus"></i>Transport Edit</a>'

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=confirmed&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Order request [PDF]</a>'

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=confirmed&format=xls" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-excel-o"></i>Order request [Excel]</a>'

                    html += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#modal-accept" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-check"></i>Complete Transport</a>'

                    return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.transporterName + ' - ' + row.supplierName + ' - [' + row.purchaseDate + '] - ' + totalItems + '</span></h5></div><div class="action-container pull-right">' + html + '</div</div></div></div>';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "transporterCode",
            "visible": false
        }, {
            targets: 2,
            "data": "transporterName",
            "visible": false
        }, {
            targets: 3,
            "data": "purchaseDate",
            "visible": false
        }, {
            targets: 4,
            data: 'orderItem',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }, {
            targets: 5,
            "data": "createdDate",
            "visible": false
        }],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
            $('span#confirmTransportItemCount').html(transportItemCount);
            transportChassisNoMatchIndexArr = getUnique(transportChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < transportChassisNoMatchIndexArr.length; i++) {
                var row = table_confirm.row(transportChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = transportChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');

                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            transportChassisNoMatchIndexArr = []
        }

    });
    table_confirm.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_confirm.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_confirm.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_confirm.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_confirm.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_confirm.rows({
            selected: true,
            page: 'current'
        }).count() !== table_confirm.rows({
            page: 'current'
        }).count()) {
            $(table_confirm_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_confirm_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_confirm_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_confirm_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_confirm.rows({
            selected: true,
            page: 'current'
        }).count() !== table_confirm.rows({
            page: 'current'
        }).count()) {
            $(table_confirm_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_confirm_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_confirm_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_confirm_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    // Customize Datatable
    $('#table-confirm-filter-search').keyup(function() {
        transportItemCount = 0;
        table_confirm.search($(this).val()).draw();
    });
    $('#table-confirm-filter-search-chassisNo').keyup(function(e) {
        transportItemCount = 0;
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            table_confirm.search($(this).val()).draw();
        }

    });
    $('#table-confirm-filter-length').change(function() {
        table_confirm.page.len($(this).val()).draw();
    });
    table_confirm.on('click', 'td>.container-fluid>.row>.col-md-12>div.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_confirm.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_confirm.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_confirm.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });
    //Completed transport details
    var table_completed_ele = $('#table-completed-transport');
    var table_completed = $(table_completed_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "autowidth": false,
        "ajax": {
            'url': myContextPath + "/transport/completed/list/datasource",
            'data': function(data) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                data["show"] = selected;
                return data;
            },
            beforeSend: function() {
                $('div#transport-completed>.overlay').show()
            },
            complete: function() {
                $('div#transport-completed>.overlay').hide();
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
            "width": "90%",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var showAcceptBtn = false;
                    for (var i = 0; i < row.orderItem.length; i++) {
                        if (row.orderItem[i].status != 1) {
                            continue;
                        }
                        showAcceptBtn = true;
                        break;
                    }
                    var totalItems = row.orderItem.length;
                    var html = ''
                    // var customData = ifNotValid(row.pickupLocation, '').toUpperCase() == 'OTHERS' ? ifNotValid(row.pickupLocation, '') : ifNotValid(row.sPickupLocation, '');
                    if ($('input[name="radioShowTable"]:checked').val() == 3) {
                        html += '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" data-toggle="modal" data-target="#modal-edit-transport"><i class="fa fa-fw fa-bus"></i>Transport Edit</a>'
                    }

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=delivered&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Order request [PDF]</a>'

                    html += '<a href="' + myContextPath + '/transport/order/request?invoiceNo=' + row.invoiceNo + '&purchaseDate=' + row.purchaseDate + '&transporter=' + row.transporterCode + '&flag=delivered&format=xls" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-excel-o"></i>Order request [Excel]</a>'

                    return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.transporterName + ' - ' + row.supplierName + ' - [' + row.purchaseDate + '] - ' + totalItems + '</span></h5></div><div class="action-container pull-right">' + html + '</div</div></div></div>';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "transporterCode",
            "visible": false
        }, {
            targets: 2,
            "data": "transporterName",
            "visible": false
        }, {
            targets: 3,
            "data": "purchaseDate",
            "visible": false
        }, {
            targets: 4,
            data: 'orderItem',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }, {
            targets: 5,
            "data": "createdDate",
            "visible": false
        }],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
            $('span#completeTransportItemCount').html(transportItemCount);
            transportChassisNoMatchIndexArr = getUnique(transportChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < transportChassisNoMatchIndexArr.length; i++) {
                var row = table_completed.row(transportChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = transportChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            transportChassisNoMatchIndexArr = []
        }

    });
    table_completed.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_completed.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_completed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_completed.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_completed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_completed.rows({
            selected: true,
            page: 'current'
        }).count() !== table_completed.rows({
            page: 'current'
        }).count()) {
            $(table_completed_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_completed_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_completed_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_completed_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_completed.rows({
            selected: true,
            page: 'current'
        }).count() !== table_completed.rows({
            page: 'current'
        }).count()) {
            $(table_completed_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_completed_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_completed_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_completed_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    // Customize Datatable
    $('#table-completed-filter-search').keyup(function() {
        transportItemCount = 0;
        table_completed.search($(this).val()).draw();
    });
    $('#table-completed-filter-search-chassisNo').keyup(function(e) {
        transportItemCount = 0;
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            table_completed.search($(this).val()).draw();
        }
    });
    $('#table-completed-filter-length').change(function() {
        table_completed.page.len($(this).val()).draw();
    });
    table_completed.on('click', 'td>.container-fluid>.row>.col-md-12>div.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_completed.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_completed.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_completed.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    //rearrange table
    var table_rearrange_ele = $('#table-transport-rearrange');
    var table_rearrange = $(table_rearrange_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/transport/rearrange/list/datasource",
            beforeSend: function() {
                $('div#transport-details-rearrange>.overlay').show()
            },
            complete: function() {
                $('div#transport-details-rearrange>.overlay').hide();
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
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 2,
            "data": "lotNo"
        }, {
            targets: 3,
            "data": "chassisNo"
        }, {
            targets: 4,
            "data": "category"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "model"
        }, {
            targets: 6,
            "data": "pickupLocationName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.pickupLocationCustom
                } else {
                    return data;
                }
            }
        }, {
            targets: 7,
            "data": "dropLocationName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.dropLocationCustom
                } else {
                    return data;
                }
            }
        }, {
            targets: 8,
            "data": "destinationCountry",
        }, {
            targets: 9,
            "data": "reason",

        }, {
            targets: 10,
            "data": "lastModifiedDate",
            "visible": false,

        }]

    });

    table_rearrange.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_rearrange.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_rearrange.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_rearrange.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_rearrange.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_rearrange.rows({
            selected: true,
            page: 'current'
        }).count() !== table_rearrange.rows({
            page: 'current'
        }).count()) {
            $(table_rearrange_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_rearrange_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_rearrange_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_rearrange_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_rearrange.rows({
            selected: true,
            page: 'current'
        }).count() !== table_rearrange.rows({
            page: 'current'
        }).count()) {
            $(table_rearrange_ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_rearrange_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_rearrange_ele).find("th.select-checkbox>input").addClass("selected");
            $(table_rearrange_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    $('#table-filter-search-rearrange').keyup(function() {
        table_rearrange.search($(this).val()).draw();
    });
    $('#table-filter-length-rearrange').change(function() {
        table_rearrange.page.len($(this).val()).draw();
    });
    $('#table-confirm-transport').on('click', 'a.btn-transport-complete', function() {
        if (!confirm($.i18n.prop('confirm.transport.completed'))) {
            return false;
        }
        var element = $(this);
        var tr = $(element).closest('tr');
        var rowData = tr.attr('data-json');
        rowData = JSON.parse(rowData);
        var params = '?invoiceId=' + rowData.invoiceNo
        var data = {};
        data["id"] = rowData.invoiceId;
        data["stockNo"] = $(element).closest('tr').find('input[name="stockNo"]').val();
        var actionUrl = "/transport/order/item/completed" + params;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + actionUrl,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var tr = $(element).closest('.clone-element').closest('tr').prev('tr');
                    var row = table_confirm.row(tr);
                    $(element).closest('tr').remove();
                    if (!isEmpty(data.data.updatedData)) {
                        row.data(data.data.updatedData).invalidate();
                        if (row.child.isShown()) {
                            row.child.hide();
                            tr.removeClass('shown');
                            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                        }
                    } else {
                        row.remove();
                        table_confirm.draw();
                    }
                    table_confirm.ajax.reload();
                }

            }

        });
    });

    //update transportation status
    var statusUpdateEle;
    $('#modal-reason').on('show.bs.modal', function(event) {
        statusUpdateEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-reason-submit', function(event) {

        var wantRearrange = false;
        if (confirm($.i18n.prop('confirm.transport.rearrangement'))) {
            wantRearrange = true;
        }
        var data = {};

        var flag = $(statusUpdateEle).data("flag");

        var actionUrl = '';

        var tr = $(statusUpdateEle).closest('tr');
        var rowData = tr.attr('data-json');
        rowData = JSON.parse(rowData);
        var params = '?invoiceId=' + rowData.invoiceNo
        data["id"] = rowData.invoiceId;
        data["stockNo"] = $(statusUpdateEle).closest('tr').find('input[name="stockNo"]').val();
        actionUrl = "/transport/order/item/cancel" + params;

        data["flag"] = flag;

        data["reason"] = $('#modal-reason').find('textarea[name="reason"]').val();
        data["rearrange"] = wantRearrange;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + actionUrl,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var tr = $(statusUpdateEle).closest('.clone-element').closest('tr').prev('tr');
                    let datatable;
                    if (tr.closest('table').attr('id') == 'table-transport') {
                        datatable = table;
                    } else if (tr.closest('table').attr('id') == 'table-confirm-transport') {
                        datatable = table_confirm;
                    } else if (tr.closest('table').attr('id') == 'table-completed-transport') {
                        datatable = table_completed;
                    }
                    var row = datatable.row(tr);
                    $(statusUpdateEle).closest('tr').remove();
                    if (!isEmpty(data.data.updatedData)) {
                        row.data(data.data.updatedData).invalidate();
                    } else {
                        row.remove();
                        datatable.draw();
                    }
                }

                $('#modal-reason').modal('toggle');
            }
        });

    })
    var radioSchedule = $('#modal-arrange-transport,#modal-edit-transport').find('input[type="radio"][name="selectedtype"].minimal,input[type="radio"][name="selecteddate"].minimal');
    radioSchedule.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    //$('#modal-arrange-transport').find('input[type="radio"][name="selecteddate"].minimal').addClass('hidden')
    //arrange transport
    $('#modal-arrange-transport').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (table_rearrange.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = table_rearrange.rows({
            selected: true,
            page: 'current'
        }).data();
        var isValid = isValidSelection(rowdata);
        if (!isValid) {
            alert($.i18n.prop('page.purchased.invoice.confirm.validation.samepurchasedate'))
            return e.preventDefault();
        }

        var supplierCode = rowdata[0].supplierCode;
        var purchaseDate = rowdata[0].sDate;
        $(this).find('input[name="purchaseDate"]').val(purchaseDate)
        $(this).find('input[name="supplierCode"]').val(supplierCode)

        $(this).find('#auto-date').iCheck('check').trigger('ifChecked');

        var element;
        var i = 0;
        table_rearrange.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table_rearrange.row(this).data();
            var transportCount = ifNotValid(data.transportationCount, '');
            if (transportCount > 0) {
                $('#modal-arrange-transport').find('#schedule-date').addClass('hidden');
                $('#modal-arrange-transport').find('#selectedDate').addClass('hidden')
            }
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(data.lotNo, '');
            var posNo = ifNotValid(data.auctionInfoPosNo, '');
            var numberPlate = ifNotValid(data.numberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort, '');
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var pickupLocation = ifNotValid(data.pickupLocation, '');
            var pickupLocationCustom = ifNotValid(data.pickupLocationCustom, '');
            var dropLocation = ifNotValid(data.dropLocation, '');
            var dropLocationCustom = ifNotValid(data.dropLocationCustom, '');
            var transporter = ifNotValid(data.transporter, '');
            var posNos = ifNotValid(data.posNos, '');

            var charge = ifNotValid(data.charge, '');

            if (i != 0) {
                element = $('#item-vechicle-clone').find('.item-vehicle').clone();
                $(element).appendTo('#item-vehicle-clone-container');
                $('.charge').autoNumeric('init');
            } else {
                element = $('#item-vehicle-container').find('.item-vehicle');
            }
            $(element).find('.select2-select').select2({
                allowClear: true,
                width: '100%'
            });
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('select[name="posNo"]').val(posNo);
            $(element).find('input[name="numberPlate"]').val(numberPlate);
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
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);
            $(element).find('input[name="data"]').val(transporter);

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
        $(element).find('.select2-select').select2({
            allowClear: true,
            width: '100%'
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-vehicle-container').find('#item-vehicle-clone-container').html('');
        $(this).find('.hidePosNo').removeClass('hidden');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[type="text"]').val('');
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#transport-remark').find('textarea').val('');
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2').val('').trigger('change');
    }).on('change', '.select2.with-others', function() {
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.others-input-container').removeClass('hidden');
            $(closest_container).find('.select2').addClass('hidden');
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
        $(closest_container).find('select.select2').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('.select2').removeClass('hidden');
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
                    text: item.name,
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
                    text: item.name,
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
                setAutonumericValue(charge, ifNotValid(valData.data.amount, 0));
            }
        } else {
            setAutonumericValue(charge, 0);

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
            }).val('').trigger('change');

        }
    });

    $('#modal-arrange-transport #item-vehicle-container, #modal-edit-transport #item-vehicle-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $('#btn-create-transport-order').on('click', function() {
        if (!$('#transport-arrangement-form').valid()) {
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
                    table_rearrange.ajax.reload();
                    $('#modal-arrange-transport').modal('toggle');
                }

            }
        });
    })
    var modalAcceptElement = $('#modal-accept');
    var modalAcceptTriggerBtnEle
    $(modalAcceptElement).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        modalAcceptTriggerBtnEle = $(e.relatedTarget);
        var data = table_confirm.row($(modalAcceptTriggerBtnEle).closest('tr')).data();
        $(this).find('#rowData').attr('data-json', JSON.stringify(data));
        var item_tr_ele = $(this).find('table>tbody>tr.clone.hide');
        var count = 0;
        for (var i = 0; i < data.orderItem.length; i++) {
            if (data.orderItem[i].status != 2) {
                continue;
            }
            if (data.orderItem[i].stockStatus != 0) {
                var element = $(item_tr_ele).clone();
                element.attr('data-json', JSON.stringify(data.orderItem[i]));
                $(element).find('input[name="amount"]').autoNumeric('init');
                $(element).find('td.sno').html(count + 1);
                $(element).find('td.stockNo>span').html(data.orderItem[i].stockNo);
                $(element).find('td.stockNo>input[name="stockNo"]').val(data.orderItem[i].stockNo);
                $(element).find('td.shuppinNo').html(data.orderItem[i].lotNo);
                $(element).find('td.chassisNo').html(data.orderItem[i].chassisNo);
                $(element).find('td.pickupLocation').html(data.orderItem[i].sPickupLocation);
                $(element).find('td.dropLocation').html(data.orderItem[i].sDropLocation);
                $(element).find('input[name="orderId"]').val(ifNotValid(data.orderItem[i].invoiceNo, ''));
                $(element).find('input[name="transporterId"]').val(ifNotValid(data.orderItem[i].transporter, ''));
                $(element).find('input[name="amount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data.orderItem[i].charge, 0));
                count++;
                $(element).removeClass('clone hide').addClass('delete-on-close data')
                $(element).appendTo($(this).find('table>tbody'));
            } else {
                alert($.i18n.prop('alert.transportation.confirm') + ': ' + data.orderItem[i].stockNo)
                return false;
            }
        }
        $(this).find('input[name="amount"]').trigger('keyup');
        // Date picker
        $(this).find('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        }).on('change', function() {
            $(this).valid();

        })
        $(this).find('input.autonumber').autoNumeric('init');
    }).on('hidden.bs.modal', function() {
        $(this).find('table>tbody>tr.delete-on-close').remove();
        $(this).find('#subTotal').val('');
        $(this).find('#taxIncuded').val('');
    }).on('click', '.btn-remove-item', function() {
        if (modalAcceptElement.find('tr.data').length > 1) {
            $(this).closest('tr').remove();
        }
        var total = 0;
        $(modalAcceptElement).find('input[name="amount"]').each(function() {
            total += Number($(this).autoNumeric('get'));
        })
        $(modalAcceptElement).find('#subTotal').html(total);
        var tax = $.i18n.prop('common.tax');
        var taxAmount = Number((total * tax) / 100);
        var totalTaxIncluded = Number((total + taxAmount));
        $(modalAcceptElement).find('#taxIncuded').html(totalTaxIncluded);
    }).on('keyup', 'input[name="amount"]', function() {
        var total = 0;
        $(modalAcceptElement).find('input[name="amount"]').each(function() {
            total += Number($(this).autoNumeric('init').autoNumeric('get'));
        })
        var tax = $.i18n.prop('common.tax');
        var taxAmount = Number((total * tax) / 100);
        var totalTaxIncluded = Number((total + taxAmount));
        $(modalAcceptElement).find('#subTotal>span').autoNumeric('init').autoNumeric('set', total);
        $(modalAcceptElement).find('#taxIncuded>span').autoNumeric('init').autoNumeric('set', totalTaxIncluded);
    }).on('click', '#btn-save-invoice', function() {
        if (!confirm($.i18n.prop('confirm.transport.completed'))) {
            return false;
        }
        if (!modalAcceptElement.find('#acceptForm').find('input').valid()) {
            return false;
        }
        var dataArray = [];
        autoNumericSetRawValue($('input.autonumber'))
        var invoiceNo = $(modalAcceptElement).find('input[name="invoiceNo"]').val();
        var rowData = $(modalAcceptElement).find('#rowData').attr('data-json');
        rowData = JSON.parse(rowData);
        var transporter = ifNotValid(rowData.transporterCode);
        var date = ifNotValid(rowData.createdDate);
        var invoiceId = ifNotValid(rowData.invoiceNo);
        var params = 'invoiceId=' + invoiceId
        //         var transporterId = $(modalAcceptElement).find('input[name="transporterId"]').val();
        $(modalAcceptElement).find('table>tbody>tr.data').each(function() {
            var rowData = $(this).attr('data-json');
            rowData = JSON.parse(rowData);
            var data = {};
            data.refNo = ifNotValid(invoiceNo, '');
            data.orderId = ifNotValid($(this).find('input[name="orderId"]').val(), '')
            data.transporterId = ifNotValid($(this).find('input[name="transporterId"]').val(), '')
            data.stockNo = ifNotValid($(this).find('input[name="stockNo"]').val(), '')
            data.etd = ifNotValid($(this).find('input[name="etd"]').val(), '')
            data.amount = ifNotValid($(this).find('input[name="amount"]').val(), '')
            data.pickupLocation = ifNotValid(rowData.pickupLocation, '');
            data.pickupLocationCustom = ifNotValid(rowData.pickupLocationCustom, '');
            data.dropLocation = ifNotValid(rowData.dropLocation, '');
            data.dropLocationCustom = ifNotValid(rowData.dropLocationCustom, '');
            dataArray.push(data);
        })

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(dataArray),
            url: myContextPath + "/transport/create/invoice?" + params,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var tr = $(modalAcceptTriggerBtnEle).closest('tr');
                    var row = table_confirm.row(tr);
                    row.data(data.data.updatedData).invalidate();
                    if (row.child.isShown()) {
                        row.child.hide();
                        $(row.node()).removeClass('shown');
                        $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                    }
                    $(modalAcceptElement).modal('toggle');
                    table_confirm.ajax.reload();
                }

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

    $('#modal-edit-transport').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }

        var element;
        var selectedDate;
        var selectedTime;
        var scheduleType
        var j = 0;
        table_confirm
        var rowdata = table.rows(e.relatedTarget.closest('tr')).data();
        if (isEmpty(rowdata[0])) {
            rowdata = table_confirm.rows(e.relatedTarget.closest('tr')).data();
        }
        if ($('input[name="radioShowTable"]:checked').val() == 3) {
            rowdata = table_completed.rows(e.relatedTarget.closest('tr')).data();
        }
        //var isValid = isValidSelection(rowdata[0].orderItem);
        for (var i = 0; i < rowdata[0].orderItem.length; i++) {
            var data = rowdata[0].orderItem[i];
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(data.lotNo, '');
            var posNo = ifNotValid(data.auctionInfoPosNo, '');
            var numberPlate = ifNotValid(data.numberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort, '');
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var pickupLocation = ifNotValid(data.pickupLocation, '');
            var pickupLocationCustom = ifNotValid(data.pickupLocationCustom, '');
            var dropLocation = ifNotValid(data.dropLocation, '');
            var dropLocationCustom = ifNotValid(data.dropLocationCustom, '');
            var transporter = ifNotValid(data.transporter, '');
            var charge = ifNotValid(data.charge, '');
            var remarks = ifNotValid(data.remarks, '');
            var comment = ifNotValid(data.comment, '');
            var id = ifNotValid(data.invoiceId, '');
            var deliveryDate = ifNotValid(data.deliveryDate, '');
            var deliveryTime = ifNotValid(data.deliveryTime, '');
            var pickupDate = ifNotValid(data.pickupDate, '');
            var pickupTime = ifNotValid(data.pickupTime, '');
            selectedDate = ifNotValid(data.selectedDate, '');
            scheduleType = ifNotValid(data.scheduleType, '');
            var forwarder = ifNotValid(data.forwarder, '');
            var posNos = ifNotValid(data.posNos, '');
            var transportCategory = ifNotValid(data.transportCategory, '');
            var etdDate = ifNotValid(data.etd, '');

            if (j != 0) {
                element = $('#modal-edit-transport #item-vechicle-clone').find('.item-vehicle').clone();
                $(element).appendTo('#modal-edit-transport #item-vehicle-clone-container');
                //                 $('.charge').autoNumeric('init');
            } else {
                element = $('#modal-edit-transport #item-vehicle-container').find('.item-vehicle');
            }
            if ($('input[name="radioShowTable"]:checked').val() == 3) {
                $(element).find('#arrivalDate').removeClass('hidden')
            } else {
                $(element).find('#arrivalDate').addClass('hidden')
            }
            $(element).find('input[name="etd"]').val(etdDate).datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            }).on('change', function() {
                $(this).valid();

            })
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('input[name="posNo"]').val(posNo);
            $(element).find('input[name="numberPlate"]').val(numberPlate);
            $(element).find('select[name="forwarder"]').val(forwarder).trigger('change');
            $(element).find('input[name="transportCategory"]').val(transportCategory);
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);
            $(element).find('input[name="data"]').val(transporter);
            $(element).find('input[name="id"]').val(id);
            
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

            $(element).find('select[name="transporter"]').val(transporter).trigger('change');
            setAutonumericValue(element.find('input[name="charge"]'), ifNotValid(charge, 0));
            $(element).find('input[name="remarks"]').val(remarks);
            j++;
        }
        $('#modal-edit-transport').find('#transport-comment').find('textarea[name="comment"]').val(comment);
        $('#modal-edit-transport').find('.modal-header').find('input[name="pickupDate"]').val(pickupDate);
        $('#modal-edit-transport').find('.modal-header').find('input[name="deliveryDate"]').val(deliveryDate);
        $('#modal-edit-transport').find('.modal-header').find('input[name="pickupTime"]').val(pickupTime);
        $('#modal-edit-transport').find('.modal-header').find('input[name="deliveryTime"]').val(deliveryTime);
        if (selectedDate == 1) {
            $('#modal-edit-transport').find('#selectedDate').find('input[name="selecteddate"]#specified-date').iCheck('check').trigger('change');
            $('#modal-edit-transport #transport-schedule-details').find('#schedule-date').removeClass('hidden')
            $('#modal-edit-transport').find('#schedule-date').find('input[name="deliveryDate"]').val(deliveryDate);
            $('#modal-edit-transport').find('#schedule-date').find('select[name="deliveryTime"]').val(deliveryTime);
            $('#modal-edit-transport').find('#schedule-date').find('input[name="pickupDate"]').val(pickupDate);
            $('#modal-edit-transport').find('#schedule-date').find('select[name="pickupTime"]').val(pickupTime);
        } else {
            $('#modal-edit-transport').find('#auto-date').iCheck('check').trigger('ifChecked');
            $('#modal-edit-transport #transport-schedule-details').find('#schedule-date').addClass('hidden')
            $('#modal-edit-transport').find('#schedule-date').find('input[name="deliveryDate"]').val('');
            $('#modal-edit-transport').find('#schedule-date').find('input[name="pickupDate"]').val('');
        }
        if (scheduleType == 1) {
            $('#modal-edit-transport').find('#selectedtype').find('input[name="selectedtype"]#urgent').iCheck('check').trigger('change');
        } else {
            $('#modal-edit-transport').find('#selectedtype').find('input[name="selectedtype"]#asap').iCheck('check').trigger('change');
        }
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-vehicle-container').find('#item-vehicle-clone-container').html('');
        $(this).find('.hidePosNo').removeClass('hidden');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[type="text"]').val('');
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
        var selectedDate = $(this).val();
        if (selectedDate == 0) {
            $('#modal-edit-transport #transport-schedule-details').find('#schedule-date').addClass('hidden')
        } else if (selectedDate == 1) {
            $('#modal-edit-transport #transport-schedule-details').find('#schedule-date').removeClass('hidden')
            var deliveryDate = ifNotValid($('#modal-edit-transport').find('.modal-header').find('input[name="deliveryDate"]').val());
            var pickupDate = ifNotValid($('#modal-edit-transport').find('.modal-header').find('input[name="pickupDate"]').val());

            $('#modal-edit-transport #deliveryDate').val(deliveryDate).datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            }).on('change', function() {
                $(this).valid();
            });
            $('#modal-edit-transport #pickupDate').datepicker('setDate', pickupDate);
            $('#modal-edit-transport #deliveryDate').datepicker('setDate', deliveryDate);
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
        if (!isEmpty(transporterData) && !autopopulate) {
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
                setAutonumericValue(charge, ifNotValid(valData.data.amount, 0));
            }
        } else {
            setAutonumericValue(charge, 0);

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
            }).val('').trigger('change');

        }
    });

    $('#btn-edit-transport-order').on('click', function() {
        if (!$('#modal-edit-transport #transport-arrangement-form').find('input,select').valid()) {
            return;
        }
        var objectArr = [];
        var data = {};
        autoNumericSetRawValue($('#modal-edit-transport').find('.charge'))
        var scheduleDetails = getFormData($('#modal-edit-transport #transport-schedule-details').find('.schedule-data'));
        var selectedType = getFormData($('#modal-edit-transport #transport-schedule-details').find('.selected-type'));
        var transportComment = getFormData($('#modal-edit-transport #transport-comment').find('.comment'));
        $("#modal-edit-transport #item-vehicle-container").find('.item-vehicle').each(function() {
            var object = {};
            object = getFormData($(this).find('input,select,textarea'));
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
            url: myContextPath + "/transport/order/update",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    table_confirm.ajax.reload();
                    table_completed.ajax.reload();
                    $('#modal-edit-transport').modal('toggle');
                }

            }
        });
    })

})
function format(rowData) {
    var element = $('#clone-container>#transport-order-item>.clone-element').clone();
    $(element).find('input[name="transportOrderNo"]').val(rowData.code)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    // $(element).find('table>thead th.arrival-date').addClass('hidden');
    for (var i = 0; i < rowData.orderItem.length; i++) {
        var row = $(rowClone).clone();
        // row.find('td.arrival-date').addClass('hidden');
        row.attr('data-json', JSON.stringify(rowData.orderItem[i]))

        var status;
        var className;
        var stockClassName;
        var stockStatus;
        if (rowData.orderItem[i].status == 1) {
            status = "Initiated"
            className = "label-default"

        } else if (rowData.orderItem[i].status == 2) {
            status = "Transport Confirmed"
            className = "label-info"
        } else if (rowData.orderItem[i].status == 3) {
            status = "Cancelled"
            className = "label-danger"
        } else if (rowData.orderItem[i].status == 5) {
            var selected = $('input[name="radioShowTable"]:checked').val();
            if (selected == 3) {
                status = "In-trasit"
            } else if (selected == 4) {
                status = "Delivered"
            }
            className = "label-success"

        }
        if (rowData.orderItem[i].stockStatus == 0) {
            stockStatus = "No"
            stockClassName = "label-default"

        } else if (rowData.orderItem[i].stockStatus == 1) {
            stockStatus = "Yes"
            stockClassName = "label-success"

        }

        var actionHtml = ''
        //         if (rowData.orderItem[i].status == 2) {
        //             actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs btn-transport-complete" ><i class="fa fa-fw fa-check-circle-o"></i>Done</a>'
        //         }
        if (rowData.orderItem[i].status != 6) {
            actionHtml += '<a href="#" class="ml-5 btn btn-danger btn-xs stock status-update" data-flag="specific" data-orderId="' + rowData.code + '" data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-close"></i>Cancel</a>'
        }
        row.attr('data-id', rowData.orderItem[i].invoiceId)
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.purchasedDate').html(ifNotValid(rowData.orderItem[i].purchaseDate, ''));
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.orderItem[i].chassisNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.orderItem[i].stockNo, ''));
        $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.auctionCompany').html(ifNotValid(rowData.orderItem[i].supplierName, ''));
        $(row).find('td.auctionHouse').html(ifNotValid(rowData.orderItem[i].auctionHouse, ''));
        //         $(row).find('td.category').html(ifNotValid(rowData.orderItem[i].category, ''));
        //         $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.pickupLocation').html((ifNotValid(rowData.orderItem[i].pickupLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sPickupLocation, '') : ifNotValid(rowData.orderItem[i].pickupLocationCustom, '')));
        $(row).find('td.dropLocation').html((ifNotValid(rowData.orderItem[i].dropLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sDropLocation, '') : ifNotValid(rowData.orderItem[i].dropLocationCustom, '')));
        $(row).find('td.lotNo').html(ifNotValid(rowData.orderItem[i].lotNo, ''));
        $(row).find('td.finalDestination').html(ifNotValid(rowData.orderItem[i].finalDestination, ''));
        $(row).find('td.etd').html(ifNotValid(rowData.orderItem[i].etd, ''));
        $(row).find('td.charge>span').html(ifNotValid(rowData.orderItem[i].charge, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData.orderItem[i].remarks, ''));
        $(row).find('td.stockStatus>span').addClass(stockClassName).html(stockStatus);
        $(row).find('td.transportationCount').html(ifNotValid(rowData.orderItem[i].transportationCount, 1));
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).find('td.action').html(actionHtml);
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
function formatConfirmTransport(rowData) {
    var element = $('#clone-container>#transport-order-item>.clone-element').clone();
    $(element).find('input[name="transportOrderNo"]').val(rowData.code)
    $(element).find('table>thead th.arrival-date').addClass('hidden');

    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.orderItem.length; i++) {
        var row = $(rowClone).clone();
        //row.find('table>tbody td.etd,td.arrival-date').addClass('hidden');
        row.attr('data-json', JSON.stringify(rowData.orderItem[i]))
        var status;
        var className;
        var stockClassName;
        var stockStatus;
        if (rowData.orderItem[i].status == 1) {
            status = "Initiated"
            className = "label-default"

        } else if (rowData.orderItem[i].status == 2) {
            status = "Transport Confirmed"
            className = "label-info"
        } else if (rowData.orderItem[i].status == 3) {
            status = "Cancelled"
            className = "label-danger"
        } else if (rowData.orderItem[i].status == 5) {
            status = "Delivered"
            className = "label-success"

        }

        if (rowData.orderItem[i].stockStatus == 0) {
            stockStatus = "No"
            stockClassName = "label-default"

        } else if (rowData.orderItem[i].stockStatus == 1) {
            stockStatus = "Yes"
            stockClassName = "label-success"

        }

        var actionHtml = ''
        //         if (rowData.orderItem[i].status == 2) {
        //             actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs btn-transport-complete" ><i class="fa fa-fw fa-check-circle-o"></i>Done</a>'
        //         }
        if (rowData.orderItem[i].status != 5) {
            actionHtml += '<a href="#" class="ml-5 btn btn-danger btn-xs stock status-update" data-flag="specific" data-orderId="' + rowData.code + '" data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-close"></i>Cancel</a>'
        }

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.purchasedDate').html(ifNotValid(rowData.orderItem[i].purchaseDate, ''));
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.orderItem[i].chassisNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.orderItem[i].stockNo, ''));
        $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.auctionCompany').html(ifNotValid(rowData.orderItem[i].supplierName, ''));
        $(row).find('td.auctionHouse').html(ifNotValid(rowData.orderItem[i].auctionHouse, ''));
        //         $(row).find('td.category').html(ifNotValid(rowData.orderItem[i].category, ''));
        //         $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.pickupLocation').html((ifNotValid(rowData.orderItem[i].pickupLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sPickupLocation, '') : ifNotValid(rowData.orderItem[i].pickupLocationCustom, '')));
        $(row).find('td.dropLocation').html((ifNotValid(rowData.orderItem[i].dropLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sDropLocation, '') : ifNotValid(rowData.orderItem[i].dropLocationCustom, '')));
        $(row).find('td.lotNo').html(ifNotValid(rowData.orderItem[i].lotNo, ''));
        $(row).find('td.finalDestination').html(ifNotValid(rowData.orderItem[i].finalDestination, ''));
        $(row).find('td.charge>span').html(ifNotValid(rowData.orderItem[i].charge, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData.orderItem[i].remarks, ''));
        $(row).find('td.transportationCount').html(ifNotValid(rowData.orderItem[i].transportationCount, 0) + 1);
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).find('td.stockStatus>span').addClass(stockClassName).html(stockStatus);
        $(row).find('td.action').html(actionHtml);
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
function formatCompletedTransport(rowData) {
    var element = $('#clone-container>#transport-order-item>.clone-element').clone();
    $(element).find('input[name="transportOrderNo"]').val(rowData.code)
    $(element).find('table>thead').find('th.transportationCount').addClass('hidden');
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    $(element).find('table>thead').find('th.action').addClass('hidden');
    for (var i = 0; i < rowData.orderItem.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-json', JSON.stringify(rowData.orderItem[i]))
        var status;
        var className;
        var stockStatus;
        var stockClassName;
        if (rowData.orderItem[i].status == 1) {
            status = "Initiated"
            className = "label-default"

        } else if (rowData.orderItem[i].status == 2) {
            status = "Transport Confirmed"
            className = "label-info"
        } else if (rowData.orderItem[i].status == 3) {
            status = "Cancelled"
            className = "label-danger"
        } else if (rowData.orderItem[i].status == 5) {
            var selected = $('input[name="radioShowTable"]:checked').val();
            if (selected == 3) {
                status = "In-trasit"
            } else if (selected == 4) {
                status = "Delivered"
            }
            className = "label-success"

        }

        if (rowData.orderItem[i].stockStatus == 0) {
            stockStatus = "No"
            stockClassName = "label-default"
        } else if (rowData.orderItem[i].stockStatus == 1) {
            stockStatus = "Yes"
            stockClassName = "label-success"
        }

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.purchasedDate').html(ifNotValid(rowData.orderItem[i].purchaseDate, ''));
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.orderItem[i].chassisNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.orderItem[i].stockNo, ''));
        $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.auctionCompany').html(ifNotValid(rowData.orderItem[i].supplierName, ''));
        $(row).find('td.auctionHouse').html(ifNotValid(rowData.orderItem[i].auctionHouse, ''));
        //         $(row).find('td.category').html(ifNotValid(rowData.orderItem[i].category, ''));
        //         $(row).find('td.model').html(ifNotValid(rowData.orderItem[i].model, ''));
        $(row).find('td.pickupLocation').html((ifNotValid(rowData.orderItem[i].pickupLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sPickupLocation, '') : ifNotValid(rowData.orderItem[i].pickupLocationCustom, '')));
        $(row).find('td.dropLocation').html((ifNotValid(rowData.orderItem[i].dropLocation, '').toLowerCase() != 'others' ? ifNotValid(rowData.orderItem[i].sDropLocation, '') : ifNotValid(rowData.orderItem[i].dropLocationCustom, '')));
        $(row).find('td.lotNo').html(ifNotValid(rowData.orderItem[i].lotNo, ''));
        $(row).find('td.finalDestination').html(ifNotValid(rowData.orderItem[i].finalDestination, ''));
        $(row).find('td.etd').html(ifNotValid(rowData.orderItem[i].etd, ''));
        $(row).find('td.charge>span').html(ifNotValid(rowData.orderItem[i].charge, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData.orderItem[i].remarks, ''));
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).find('td.stockStatus>span').addClass(stockClassName).html(stockStatus);
        $(row).find('td.action').addClass('hidden');
        $(row).find('td.transportationCount').addClass('hidden');
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
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
function isPickupAndDropIsSame(element) {
    var container = $(element).closest('.item-vehicle')
    var from = container.find('select[name="pickupLocation"]').val();
    var to = container.find('select[name="dropLocation"]').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}
function confirmTransport(params) {
    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'post',
        async: false,
        data: JSON.stringify(params),
        contentType: 'application/json',
        url: myContextPath + "/transport/order/request/confirm",
        success: function(data) {
            response = data;
        }
    })
    return response;
}
function isValidSelection(data) {
    var tmpPData = '';
    var tmpPSupplier = '';
    for (var i = 0; i < data.length; i++) {
        //console.log(data[i].sDate)
        var tableData = data[i];
        if (i == 0) {
            //tmpPData = tableData.sDate;
            tmpPSupplier = tableData.supplierCode;
        } else if (!(tmpPSupplier == tableData.supplierCode)) {
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
