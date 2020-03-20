var countriesJson, japanJson, rowselectedData;
$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('input[type="checkbox"].minimal,input[type="radio"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
    })
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanJson = data;
    })

    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        setDashboard(data);
    });

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        $('#modal-edit-reserve-price #currency').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        });

    })

    var isAdminManagerValue = $('input[name="isAdminManager"]').val();
    isAdminManagerValue = (isAdminManagerValue == 1) ? true : false;
    var isAccountFreeze = $('input[name="isAccountFreeze"]').val();

    var userName = $('#userInfo').text();
    // Datatable
    var table = $('#table-stock').DataTable({

        "ajax": {
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/sales/reserve-data",
            data: function(data) {
                if (!$('input[name="showMine"]').is(':checked')) {
                    data.flag = 0;
                } else {
                    data.flag = 1;
                }
                data.stockType = $('input[name="isBidding"]:checked').val();
                return data;
            }
        },
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "order": [[8, "desc"]],
        //"order": [[2, "asc"]],
        "ordering": true,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "targets": 8,
            "type": "date-eu"
        }, {
            targets: 0,
            orderable: false,
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
            orderable: true,
            "searchable": true,
            "className": "details-control",
            "name": "stockNo",
            "data": "stockNo",
            "render": function(data, type, row) {
                var status;
                var className;
                if (row.shippingInstructionStatus == "0") {
                    status = "IDLE"
                    className = "default"
                } else if (row.shippingInstructionStatus == "1") {
                    status = "REQUEST GIVEN"
                    className = "success"
                } else if (row.shippingInstructionStatus == "2") {
                    status = "CANCELLED"
                    className = "warning"
                }
                return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a></br>' + '<span class="label label-' + ifNotValid(className, 'default') + '" style="min-width:100px">' + ifNotValid(status, '') + '</span>';
            }

        }, {
            targets: 2,
            orderable: true,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            orderable: true,
            "searchable": true,
            "data": "category"
        }, {
            targets: 4,
            orderable: true,
            "searchable": true,
            "data": "maker"
        }, {
            targets: 5,
            orderable: true,
            "searchable": true,
            "data": "model"
        }, {
            targets: 6,
            orderable: true,
            "searchable": true,
            "data": "destinationCountry",
            "render": function(data, type, row) {
                return !isEmpty(data) ? data + " / " + row.destinationPort : "";
            }
        }, {
            targets: 7,
            "className": "dt-right",
            orderable: true,
            "searchable": false,
            "data": "price",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';

            }
        }, {
            targets: 8,
            orderable: true,
            "searchable": true,
            "data": "date"
        }, {
            targets: 9,
            orderable: true,
            "searchable": true,
            "data": "date",
            "render": function(data, type, row) {
                //data = data == null ? '' : data;
                let reservedDate = !isEmpty(data) ? data : row.purchaseDate;
                let date1 = moment(reservedDate, "DD-MM-YYYY");
                let date2 = moment(new Date(), "DD-MM-YYYY");
                var diffInDays = date2.diff(date1, 'days');
                if (diffInDays > 300) {
                    $('input[name="accountFreeze"]').attr("value", 1);
                }
                return diffInDays + " days";
            }
        }, {
            targets: 10,
            orderable: true,
            "searchable": true,
            'visible': isAdminManagerValue,
            "data": "reserveBy"
        }, {
            targets: 11,
            orderable: true,
            "searchable": true,
            "data": "customerId",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (!isEmpty(data)) {
                    return row.firstName + ' ' + row.lastName;
                } else {
                    $('input[name="accountFreezeCustomer"]').attr("value", 1);
                }

            }
        }, {
            targets: 12,
            "className": "dt-right",
            orderable: true,
            "searchable": false,
            "data": "fob",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + ifNotValid("¥ ", " ") + ' " data-m-dec="0">' + data + '</span>';

            }
        }, {
            targets: 13,
            "className": "dt-right",
            orderable: true,
            "searchable": false,
            "data": "minSellingPriceInDollar",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + ifNotValid("$ ", '') + ' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';

            }
        }, {
            targets: 14,
            orderable: false,
            "data": "stockNo",
            "render": function(data, type, row) {
                var html = '';
                html += '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Edit Price" data-toggle="modal" data-target="#modal-edit-reserve-price"><i class="fa fa-fw fa-edit"></i></a>'
                return '<div style="width:100px;">' + html + '</div>';
            }
        }, {
            targets: 15,
            orderable: true,
            "searchable": false,
            "visible": false,
            "data": "firstRegDate"
        }, {
            targets: 16,
            orderable: true,
            "searchable": true,
            "visible": false,
            "data": "customerId"
        }, {
            targets: 17,
            "width": "80px",
            "visible": false,
            "className": "details-control",
            "data": "status"

        }],
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init')

        },
        //         ,"fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        //             if (aData.status == "2") {
        //                 $('td', nRow).css('background-color', '#ef503d');
        //             }
        //             if (aData.status == "1") {
        //                 $('td', nRow).css('background-color', '#ffc34d');
        //             }

        //         }
        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'ReservedStocks' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],
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
                    v: 'Reserved Vehicles'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear()
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }],

    });

    $("#excel_export_all").on("click", function() {
        table.button("#dt_excel_export_all").trigger();

    });

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });

    //    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    //        var checked = $('#showMine').is(':checked');
    //        if (checked && data[8] =='admin') {
    //            return true;
    //        }
    //        if (!checked ) {
    //            return true;
    //        }
    //       
    //        return false;
    //    });

    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $(document).on('ifChecked', '#lastLapVehiclesCheck', function() {
        table.draw();
    }).on('ifUnchecked', '#lastLapVehiclesCheck', function() {
        table.draw();
    })
    $(document).on('ifChecked', 'input[name="isBidding"]', function() {
        //             if ($(this).val() == 1) {
        //                 $('#changeBiddingSalesPerson').removeClass('hidden')
        //             } else {
        //                 $('#changeBiddingSalesPerson').addClass('hidden')
        //             }
        table.ajax.reload();
    })
    $(document).on('ifChecked', '#showMine', function() {

        $('input[name="accountFreeze"]').attr("value", "");
        $('input[name="accountFreezeCustomer"]').attr("value", "");
        table.ajax.reload()
    }).on('ifUnchecked', '#showMine', function() {
        //         $.fn.dataTable.ext.search.pop();
        //         table.draw()
        $('input[name="accountFreeze"]').attr("value", "");
        $('input[name="accountFreezeCustomer"]').attr("value", "");
        table.ajax.reload()
    })
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-reserve-date').daterangepicker({
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
        $('#table-filter-reserve-date').val('');
        $(this).remove();

    })
    var filterCustomer = $('#custselectId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //    	var checked = $('#showMine').is(':checked');
        //        if (checked && data[8] =='admin') {
        //            return true;
        //        }
        //        if (!checked ) {
        //            return true;
        //        }

        //date filter
        let lastLapVehiclesCheck = ifNotValid($('#lastLapVehiclesCheck').is(':checked'), '');
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[8].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[8], 'DD-MM-YYYY')._d.getTime();
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
        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[16].length == 0 || aData[16] != filterCustomer) {
                return false;
            }
        }
        //         if (typeof lastLapVehiclesCheck != 'undefined' && lastLapVehiclesCheck == true) {
        //             if (ifNotValid(aData[15], '0') == '0') {
        //                 return false;
        //             }
        //         }
        return true;
    });
    $('#btn-unReserve').on('click', function() {
        var alert = confirm($.i18n.prop('confirm.stock.unreserve'));
        if (alert == true) {
            var stockNo = [];
            table.rows({
                selected: true,
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var data = table.row(this).data();
                stockNo.push(data.stockNo);
                table.draw();
            })

            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "put",
                data: JSON.stringify(stockNo),
                url: myContextPath + "/stock/unReserve",
                async: false,
                contentType: "application/json",
                success: function(data) {
                    if (data.status == "success") {
                        $('#alert-block').css('display', 'block').html('<strong>' + data.status + '!</strong> ' + data.message);
                        $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                            $("#alert-block").slideUp(500);
                        });
                    } else {
                        $('#alert-block-warning').css('display', 'block').html('<strong>' + data.status + '!</strong> ' + data.message);
                        $("#alert-block-warning").fadeTo(5000, 500).slideUp(500, function() {
                            $("#alert-block-warning").slideUp(500);
                        });
                    }
                    $('input[name="accountFreeze"]').attr("value", "");
                    $('input[name="accountFreezeCustomer"]').attr("value", "");
                    table.ajax.reload();
                    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
                        setDashboard(data);
                    });
                }
            });
        }
    })

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('#btn-apply-filter').click(function() {
        table.draw();
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
    }).on("select", function(e) {
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
        var count = table.rows({
            selected: true,
            page: 'current'
        }).count();
        var actioBtn = $('button.enable-on-select')
        var valueAccount = $('input[name="accountFreeze"]').val();
        var valueAccountCustomer = $('input[name="accountFreezeCustomer"]').val();
        if (count > 0) {
            //            if (isAccountFreeze == "1") {
            //                if (!valueAccount == "1" && !valueAccountCustomer == "1") {
            //                    actioBtn.removeAttr("disabled");
            //                } else {
            //                    actioBtn.attr("disabled", "disabled");
            //                }
            //            } else {
            actioBtn.removeAttr("disabled");
            //            }
        } else {
            actioBtn.attr("disabled", "disabled");
        }

    }).on("deselect", function(e) {
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

        var count = table.rows({
            selected: true,
            page: 'current'
        }).count();
        var actioBtn = $('button.enable-on-select')
        var valueAccount = $('input[name="accountFreeze"]').val();
        var valueAccountCustomer = $('input[name="accountFreezeCustomer"]').val();
        if (count > 0) {
            //            if (isAccountFreeze == "1") {
            //                if (!valueAccount == "1" && !valueAccountCustomer == "1") {
            //                    actioBtn.removeAttr("disabled");
            //                } else {
            //                    actioBtn.attr("disabled", "disabled");
            //                }
            //            } else {
            actioBtn.removeAttr("disabled");
            //            }
        } else {
            actioBtn.attr("disabled", "disabled");
        }

    });
    var selectedRowData = [];
    table.on('click', 'input.selectBox', function(e) {
        if ($(this).is(':checked')) {
            selectedRowData.push(table.rows($(this).closest('tr')).data()[0]);
        } else {
            let remove = table.rows($(this).closest('tr')).data()[0];

            selectedRowData = jQuery.grep(selectedRowData, function(value) {
                return value != remove;
            });
        }
        var tmpIsBidding;
        var isValid = true;
        if (selectedRowData.length > 1) {
            for (var i = 0; i < selectedRowData.length; i++) {
                if (i == 0) {
                    tmpIsBidding = 0;
                } else if (tmpIsBidding != selectedRowData[i].isBidding) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                $('#btn-unReserve').removeAttr("disabled")
            } else {
                $('#btn-unReserve').attr("disabled", "disabled");
            }
        } else if (selectedRowData.length == 1) {
            if (selectedRowData[0].isBidding == 0) {
                $('#btn-unReserve').removeAttr("disabled")
            } else {
                $('#btn-unReserve').attr("disabled", "disabled");
            }
        } else {
            $('#btn-unReserve').attr("disabled", "disabled");
        }
    })

    //Calculation FOB,Freight,Insurance ,Total Amount 
    $('#item-invoice-container').on('keyup', ".calculation", function() {
        doCalculation(this)
    }).on('change', 'input[name="total"]', function() {
        doCalculation(this)
    })
    function doCalculation(element) {
        var closestEle = $(element).closest('.row');

        var fob = Number($(closestEle).find('input[name="fob"]').autoNumeric('get'));
        var insurance = Number($(closestEle).find('input[name="insurance"]').autoNumeric('get'));
        var freight = Number($(closestEle).find('input[name="freight"]').autoNumeric('get'));
        var shipping = Number($(closestEle).find('input[name="shipping"]').autoNumeric('get'));
        var totalEle = $(closestEle).find('input[name="total"]');
        var total = fob + insurance + freight + shipping;
        //$(totalEle).autoNumeric('set', total);
        var element;
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }

        var total = Number($(closestEle).find('input[name="total"]').autoNumeric('get'));
        var totalWithoutFob = insurance + freight + shipping;
        fob = total - totalWithoutFob;
        if (fob < 0) {
            alert($.i18n.prop('alert.profomainvoice.create.EqualsPrice'))
            $(element).autoNumeric('set', 0).trigger('keyup');
            return;
        }
        var fobEle = $(closestEle).find('input[name="fob"]');
        $(fobEle).autoNumeric('set', fob);

        //total
        var fobTotal = 0;
        $('#item-invoice-container').find('input[name="fob"]').each(function(index) {
            fobTotal += Number($(this).autoNumeric('get'))
        })
        var insuranceTotal = 0;
        $('#item-invoice-container').find('input[name="insurance"]').each(function(index) {
            insuranceTotal += Number($(this).autoNumeric('get'))
        })
        var freightTotal = 0;
        $('#item-invoice-container').find('input[name="freight"]').each(function(index) {
            freightTotal += Number($(this).autoNumeric('get'))
        })
        var shippingTotal = 0;
        $('#item-invoice-container').find('input[name="shipping"]').each(function(index) {
            shippingTotal += Number($(this).autoNumeric('get'))
        })
        var amountTotal = 0;
        $('#item-invoice-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })

        $('#fobTotal').autoNumeric('set', fobTotal);
        $('#insuranceTotal').autoNumeric('set', insuranceTotal);
        $('#freightTotal').autoNumeric('set', freightTotal);
        $('#shippingTotal').autoNumeric('set', shippingTotal);
        $('#grand-total').autoNumeric('set', amountTotal);

    }
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    $('#currencyType').on('change', function() {
        var data = $(this).select2('data')[0].data;
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                modalCreateProforma.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                modalCreateProforma.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }
        }
    })

    //Model Show 
    var modalCreateProforma = $('#modal-create-proforma');
    modalCreateProforma.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        //validate custome
        var selectedRowData = table.rows({
            selected: true,
            page: 'current'
        }).data()
        var tmpCustId;
        var isValid = true;
        for (var i = 0; i < selectedRowData.length; i++) {
            if (i == 0) {
                tmpCustId = selectedRowData[i].customerId;
            } else if (tmpCustId != selectedRowData[i].customerId) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            alert($.i18n.prop('alert.profomainvoice.create.selected.different.cutomer'));
            return e.preventDefault();
        }

        var element;
        var i = 0;

        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var price = ifNotValid(data.price, '');

            element = $('#item-invoice-clone').find('.item-invoice').clone();
            $(element).appendTo('#item-invoice-clone-container');

            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="firstRegDate"]').val(firstRegDate);
            $(element).find('input[name="hsCode"]').val(ifNotValid(data.hsCode, ''));
            if (data.destinationCountry == "SRI LANKA") {
                $(element).find('input[name="hsCode"]').addClass('required');
            } else {
                $(element).find('input[name="hsCode"]').removeClass('required');
            }
            $(element).find('input[name="fob"]').autoNumeric('init').autoNumeric('set', price);
            $(element).find('input[name="insurance"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="shipping"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="freight"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="total"]').autoNumeric('init').autoNumeric('set', price);
            $(element).find('.calculation').trigger('keyup');
            i++;
        });

        var data = table.rows({
            selected: true,
            page: 'current'
        }).data()[0];
        var customerId = data.customerId;
        var custIdElem = modalCreateProforma.find('select[name="customerId"]');
        if (!isEmpty(customerId)) {
            $.ajax(myContextPath + "/customer/data/" + customerId, {
                type: "get",
                contentType: "application/json",
                async: false
            }).done(function(data) {
                data = data.data
                custIdElem.select2({
                    "data": [{
                        id: data.code,
                        text: data.companyName + ' :: ' + data.firstName + ' ' + data.lastName + '(' + data.nickName + ')',
                        data: data
                    }]
                }).val(data.code).trigger('change');

            });

            customerInitSelect2(custIdElem, {
                id: data.customerId,
                text: data.companyName + ' :: ' + data.firstName + ' ' + data.lastName + '(' + data.nickName + ')',
            });
        } else {
            alert($.i18n.prop('alert.customer.noselection'))
            return false;
        }

        $(this).find('input.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        //         $(this).find('#item-invoice-container .item-invoice div.row').not(':first').remove();
        $(this).find('#item-invoice-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
    }).on('click', '.btn-remove-item', function() {
        var performaElement = $(modalCreateProforma).find('#item-invoice-clone-container div.row');
        if (performaElement.length > 1) {
            $(this).closest('div.row').remove();
            $(modalCreateProforma).find('.calculation').trigger('keyup');
        }
    }).on("change", 'select[name="customerId"]', function(event) {
        var id = $(this).val();
        if (id == null || id.length == 0) {
            modalCreateProforma.find('#cFirstName').empty();
            modalCreateProforma.find('#npFirstName').empty();
            return;
        }
        var data = $(this).select2('data')[0].data;
        var currencyEle = $('#currencyType')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change').addClass('readonly');
        } else {
            currencyEle.val('').trigger('change');
        }
        //payment type
        var data = $(this).select2('data')[0].data;
        var paymentEle = $('#paymentType')
        if (!isEmpty(data)) {
            paymentEle.val(data.paymentType).trigger('change');
        } else {
            paymentEle.val('').trigger('change');
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "GET",
            async: false,
            url: myContextPath + "/customer/data/" + id,
            success: function(data) {
                if (data.status == 'success') {
                    var selectedVal = '';
                    if (data.data.consigneeNotifyparties.length == 1) {
                        selectedVal = data.data.consigneeNotifyparties[0].id;
                    }
                    let consArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $('#cFirstName').empty();
                    $('#cFirstName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    $('#npFirstName').empty();
                    let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })
                    $('#npFirstName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

    })

    $('#item-invoice-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $('#cFirstName').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#npFirstName').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#paymentType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#paymentSalesType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    //  $.getJSON(myContextPath + "/data/currency.json", function(data) {
    //         currencyJson = data;
    //     })
    //Curency Proforma Json
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        currencyJson = data;
        $('#currencyType,#currencySalesType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        })
    })

    // Create Proforma Invoice Order

    $('#btn-generate-proforma-invoice').on('click', function() {
        if (!$('#create-proforma-form').find('input,select').valid()) {
            return;
        }
        var autoNumericElements = $('#modal-create-proforma').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        var objectArr = [];
        var data = {};
        data = getFormData($("#proforma-invoice-container").find('.invoiceData'));
        object = $('#item-invoice-container').find('.item-invoice').serialize();
        $("#item-invoice-container").find('.item-invoice').each(function() {
            object = getFormData($(this).find('input,select,textarea'));
            objectArr.push(object);
        });
        data['items'] = objectArr.filter(value=>JSON.stringify(value) !== '{}');

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/proforma/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {

                    $.redirect(myContextPath + '/sales/proformainvoice', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '1');
                }

            }
        });
    })
    var custdfdata;
    $('#modal-create-sales').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        //validate custome
        var selectedRowData = table.rows({
            selected: true,
            page: 'current'
        }).data()
        var tmpCustId;
        var isValid = true;
        for (var i = 0; i < selectedRowData.length; i++) {
            if (i == 0) {
                tmpCustId = selectedRowData[i].customerId;
            } else if (tmpCustId != selectedRowData[i].customerId) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            alert($.i18n.prop('alert.profomainvoice.create.selected.different.cutomer'));
            return e.preventDefault();
        }

        var element;
        var i = 0;
        // var custdfdata;
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            custdfdata = table.row(this).data();
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var price = ifNotValid(data.price, '');
            var custName = ifNotValid(data.firstName, '');
            var custLastName = ifNotValid(data.lastName, '');
            var customerId = ifNotValid(data.customerId, '');

            if (i != 0) {
                element = $('#item-sales-clone').find('.item-sales').clone();
                $(element).appendTo('#item-sales-clone-container');
            } else {
                element = $('#item-sales-container').find('.item-sales');
            }
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="firstRegDate"]').val(firstRegDate);
            $(element).find('input[name="total"]').autoNumeric('init').autoNumeric('set', price)
            $('#sales-details').find('#customernameSalesId').val(custName + ' ' + custLastName);
            $('#sales-details').find('#customerSalesId').val(customerId);
            i++;
        });
        $(this).find('input.autonumber').autoNumeric('init');
        var customerId = ifNotValid(custdfdata.customerId, '');
        var id = customerId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "GET",
            async: false,
            url: myContextPath + "/customer/data/" + id,
            success: function(custdfdata) {
                if (custdfdata.status == 'success') {
                    var selectedVal = '';
                    if (custdfdata.data.consigneeNotifyparties.length == 1) {
                        selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                    }
                    let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $('#cFirstsalesName').empty();
                    $('#cFirstsalesName').select2({
                        allowClear: true,
                        //width: '100%',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })

                    $('#npFirstsalesName').empty();
                    $('#npFirstsalesName').select2({
                        allowClear: true,
                        //width: '100%',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    var currencyEle = $('#currencySalesType')
                    if (!isEmpty(custdfdata)) {
                        currencyEle.val(custdfdata.data.currencyType).trigger('change').addClass('readonly');
                    } else {
                        currencyEle.val('').trigger('change');
                    }

                    var paymentTypeEle = $('#paymentSalesType')
                    if (!isEmpty(custdfdata)) {
                        paymentTypeEle.val(custdfdata.data.paymentType).trigger('change');
                    } else {
                        paymentTypeEle.val('').trigger('change');
                    }
                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })
        $('#grand-total-sales').autoNumeric('set', amountTotal);
        //$(this).find('.calculation').trigger('keyup');
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-sales-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
    });

    $('#currencySalesType').on('change', function() {
        var data = $(this).select2('data')[0].data;
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                $('#modal-create-sales').find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                $('#modal-create-sales').find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }
        }
    })

    //Total Calculation FOB,Freight,Insurance ,Total Amount 
    $('#item-sales-container').on('keyup', ".calculation", function() {
        var closestEle = $(this).closest('.row');

        var fob = Number($(closestEle).find('input[name="fob"]').autoNumeric('get'));
        var insurance = Number($(closestEle).find('input[name="insurance"]').autoNumeric('get'));
        var freight = Number($(closestEle).find('input[name="freight"]').autoNumeric('get'));
        var shipping = Number($(closestEle).find('input[name="shipping"]').autoNumeric('get'));
        var totalEle = $(closestEle).find('input[name="total"]');
        var total = fob + insurance + freight + shipping;
        //$(totalEle).val(total);
        var element;
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }

        var total = Number($(closestEle).find('input[name="total"]').autoNumeric('get'));
        var totalWithoutFob = insurance + freight + shipping;
        fob = total - totalWithoutFob;
        if (fob < 0) {
            alert($.i18n.prop('alert.profomainvoice.create.EqualsPrice'))
            $(this).autoNumeric('set', 0).trigger('keyup');
            return;
        }
        var fobEle = $(closestEle).find('input[name="fob"]');
        $(fobEle).autoNumeric('set', fob);
        //total
        var fobTotal = 0;
        $('#item-sales-container').find('input[name="fob"]').each(function(index) {
            fobTotal += Number($(this).autoNumeric('get'))
        })
        var insuranceTotal = 0;
        $('#item-sales-container').find('input[name="insurance"]').each(function(index) {
            insuranceTotal += Number($(this).autoNumeric('get'))
        })
        var freightTotal = 0;
        $('#item-sales-container').find('input[name="freight"]').each(function(index) {
            freightTotal += Number($(this).autoNumeric('get'))
        })
        var shippingTotal = 0;
        $('#item-sales-container').find('input[name="shipping"]').each(function(index) {
            shippingTotal += Number($(this).autoNumeric('get'))
        })
        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })

        $('#fobsalesTotal').autoNumeric('set', fobTotal);
        $('#insurancesalesTotal').autoNumeric('set', insuranceTotal);
        $('#freightsalesTotal').autoNumeric('set', freightTotal);
        $('#shippingsalesTotal').autoNumeric('set', shippingTotal);
        $('#grand-total-sales').autoNumeric('set', amountTotal);

    })
    // insurance field hide on C&F Payment type
    $('#proforma-invoice-container').on('change', 'select[name="paymentType"]', function() {
        var paymentType = $(this).val();
        if (paymentType === "C&F") {
            $('#item-invoice-container').find('input[name="insurance"]').val(0).prop('disabled', true);
            $('#item-invoice-container').find('input[name="insuranceTotal"]').val(0).prop('disabled', true);
        } else {
            $('#item-invoice-container').find('input[name="insurance"]').prop('disabled', false);
        }

        if (paymentType == "FOB") {
            $('#item-invoice-container').find('input[name="freight"]').val(0).prop('disabled', true);
            $('#item-invoice-container').find('input[name="freightTotal"]').val(0);
        } else {
            $('#item-invoice-container').find('input[name="freight"]').prop('disabled', false);

        }
    })

    // Create Sales Invoice Order

    $('#btn-generate-sales-invoice').on('click', function() {
        if (!$('#create-sales-form').valid()) {
            return;
        }
        var autoNumericElements = $('#modal-create-sales').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        var objectArr = [];
        var data = {};
        var invoiceDtl = getFormData($("#sales-invoice-container").find('.salesData'));
        var object;
        $("#item-sales-container").find('.item-sales').each(function() {
            object = {};
            object = getFormData($(this).find('input,select,textarea'));
            objectArr.push(object);
        });
        data['invoiceItems'] = objectArr;
        data['invoiceDtl'] = invoiceDtl;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/sales/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/sales/sales-order-invoice-list', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '4');
                } else {
                    $('#modal-create-sales').modal('toggle')
                    $('#alert-block').removeClass('alert-success').addClass('alert-danger');
                    $('#alert-block').css('display', 'block').html('<strong>Failed!</strong> ' + data.message);
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }

            }
        });
    })

    $('#paymentSalesType').on('change', function() {
        var closestEle = $(this).closest('.row');
        var id = $(this).val();
        if (id == "FOB") {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', true);
            // $('#item-sales-container').find('input[name="freight"]').val(0);
            // $('#totalSalesDiv').find('input[name="freightTotal"]').val(0);

        } else {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', false);

        }
    })
    $('#item-sales-container').slimScroll({
        start: 'bottom',
        height: ''
    });

    $('#sales-invoice-container').on('change', 'select[name="paymentType"]', function() {
        var paymentType = $(this).val();
        if (paymentType === "C&F") {
            $('#item-sales-container').find('input[name="insurance"]').prop('readonly', true);
            //$('#item-sales-container').find('input[name="insurance"]').val(0);
            //$('#totalSalesDiv').find('input[name="insuranceTotal"]').val(0);
        } else {
            $('#item-sales-container').find('input[name="insurance"]').prop('readonly', false);
        }

    })

    //filter By Customer Search
    $('#custselectId').select2({
        allowClear: true,
        placeholder: 'Search customer email',
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
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
    }).on('change', function(event) {
        filterCustomer = $(this).find('option:selected').val();
        table.draw();
    })

    $('.datePicker').datepicker({
        format: "mm/yyyy",
        autoclose: true,
        viewMode: "months",
        minViewMode: "months"
    })

    //on open Shipping modal

    $('#modal-create-shipping').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var rowdata = table.rows({
            selected: true,
            page: 'current'
        }).data();

        //check valid
        for (var i = 0; i < rowdata.length; i++) {
            if (rowdata[i].shippingInstructionStatus == 1) {
                if (rowdata.length == 1) {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }

        var element;
        var i = 0;
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var customerId = ifNotValid(data.customerId, '');
            var customerName = ifNotValid(data.firstName, '');
            var customerLastName = ifNotValid(data.lastName, '');
            var inspectionFlag = ifNotValid(data.inspectionFlag, '');

            if (i != 0) {
                element = $('#item-shipping-clone').find('.item-shipping').clone();
                element.find('input[name="estimatedType"]').attr('name', 'estimatedType' + i);
                element.find('input[name="estimatedType' + i + '"][value="0"]').iCheck('check').trigger('change')
                $(element).appendTo('#item-shipping-clone-container');
            } else {
                element = $('#item-shipping-container').find('.item-shipping');
            }
            $(element).find('input[type="radio"].estimated-data.minimal').iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            })
            $(element).find('.datePicker').datepicker({
                format: "mm/yyyy",
                autoclose: true,
                viewMode: "months",
                minViewMode: "months"
            })
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="customerId"]').val(customerId);
            $(element).find('input[name="customernameId"]').val(customerName + ' ' + customerLastName);
            i++;
            $(element).find('.estimatedDate').addClass('hidden');
            if (inspectionFlag == 1) {
                $(element).find('input[name="inspectionFlag"]').attr('checked', true);
            } else {
                $(element).find('input[name="inspectionFlag"]').attr('checked', false);
            }
            var id = customerId;
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "GET",
                async: false,
                url: myContextPath + "/customer/data/" + id,
                success: function(data) {
                    if (data.status == 'success') {
                        var selectedVal = '';
                        if (data.data.consigneeNotifyparties.length == 1) {
                            selectedVal = data.data.consigneeNotifyparties[0].id;
                        }
                        let consArray = data.data.consigneeNotifyparties.filter(function(index) {
                            return !isEmpty(index.cFirstName);
                        })
                        $(element).find('select[name="consigneeId"]').empty()
                        $(element).find('#cFirstshippingName').select2({
                            allowClear: true,
                            // width: '100%',
                            data: $.map(consArray, function(consigneeNotifyparties) {
                                return {
                                    id: consigneeNotifyparties.id,
                                    text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                };
                            })
                        }).val(selectedVal).trigger("change");
                        let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                            return !isEmpty(index.npFirstName);
                        })
                        $(element).find('select[name="notifypartyId"]').empty()
                        $(element).find('#npFirstshippingName').select2({
                            allowClear: true,
                            width: '100%',
                            data: $.map(npArray, function(consigneeNotifyparties) {
                                return {
                                    id: consigneeNotifyparties.id,
                                    text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                };
                            })
                        }).val(selectedVal).trigger("change");
                        $(element).find('select[name="destCountry"]').select2({
                            allowClear: true,
                            width: '100%',
                            data: $.map(countriesJson, function(item) {
                                return {
                                    id: item.country,
                                    text: item.country,
                                    data: item
                                };
                            })
                        }).val(data.data.country).trigger('change');
                        $(element).find('select[name="destPort"]').val(data.data.port).trigger('change');
                        $(element).find('select[name="yard"]').val(data.data.yard).trigger('change');
                        $(element).find('.datePicker').datepicker({
                            format: "dd-mm-yyyy",
                            autoclose: true
                        })
                    }
                },
                error: function(e) {
                    console.log("ERROR: ", e);
                }
            });
        });

        //Slim Scroll 
        $('#item-shipping-container').slimScroll({
            start: 'bottom',
            height: ''
        });

        //Create Shipping Instruction
        $('#item-shipping-container').find('.select2-select').select2({
            allowClear: true,
            // width: '100%',
        })

    }).on('change', 'select[name="destCountry"]', function() {
        var closestEle = $(this).closest('.item-shipping');
        var country = $(this).find('option:selected').val();
        let data = $(this).find('option:selected').data();
        let countryData = data.data;
        var portList;
        var yardList;
        $.each(countriesJson, function(i, item) {
            if (item.country == country) {
                portList = item.port;
                yardList = item.yardDetails;
                return false;
            }
        });
        if (country.toUpperCase() == 'KENYA') {
            $(closestEle).find('#yardFields').removeClass('hidden');
        } else {
            $(closestEle).find('#yardFields').addClass('hidden')
        }
        $(closestEle).find('select[name="destPort"]').empty();
        $(closestEle).find('select[name="yard"]').empty();
        if (typeof portList == 'undefined') {

            return;
        }
        $(closestEle).find('select[name="destPort"]').select2({
            allowClear: true,
            data: $.map(portList, function(item) {
                return {
                    id: item,
                    text: item
                };
            })
        }).val('').trigger("change");
        $(closestEle).find('select[name="yard"]').select2({
            allowClear: true,
            data: $.map(yardList, function(item) {
                return {
                    id: item.id,
                    text: item.yardName
                };
            })
        }).val('').trigger("change");
        if (!isEmpty(countryData.data)) {
            $(closestEle).find('input[name="inspectionFlag"]').attr('checked', countryData.data.inspectionFlag == 1 ? true : false).trigger('change');
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('#item-shipping-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
        $(this).find('input[name="inspectionFlag"]').attr('checked', false);
    }).on('ifChecked', '.estimated-data', function() {
        if ($(this).val() == 0) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
            $(this).closest('.estimatedData').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 1) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
            $(this).closest('.estimatedData').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 2) {
            $(this).closest('.estimatedData').find('.estimatedDate').removeClass('hidden');
        }

    }).on('click', '#btn-save-shipping', function(event) {
        if (!($('#create-shipping-instruction-form').find('input,select').valid())) {
            return false;
        }
        var objectArr = [];
        $("#item-shipping-container").find('.item-shipping').each(function() {
            var data = {};
            object = getFormData($(this).find('input,select,textarea'));
            estimatedTypeEle = $(this).find('.estimated-data')
            var estimatedType = '';
            for (var i = 0; i < estimatedTypeEle.length; i++) {
                if (estimatedTypeEle[i].checked) {
                    estimatedType = estimatedTypeEle[i].value;
                }
            }
            data.stockNo = object.stockNo;
            data.notifypartyId = object.notifypartyId;
            data.scheduleType = estimatedType;
            data.destPort = object.destPort;
            data.destCountry = object.destCountry;
            data.customernameId = object.customernameId;
            data.customerId = object.customerId;
            data.consigneeId = object.consigneeId;
            data.chassisNo = object.chassisNo;
            data.paymentType = object.paymentType
            data.inspectionFlag = object.inspectionFlag
            data.yard = object.yard
            data.estimatedArrival = object.estimatedArrival
            data.estimatedDeparture = object.estimatedDeparture
            objectArr.push(data);
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
            url: myContextPath + "/invoice/shipping/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/sales/status', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '5');
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
    let reservePriceEditModalEle = $('#modal-edit-reserve-price');
    reservePriceEditModalEle.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        rowselectedData = table.rows(e.relatedTarget.closest('tr')).data();
        var custometDropdownEles = $(reservePriceEditModalEle).find('select[name="custId"]');
        $(custometDropdownEles).each(function() {
            $('<option value="' + rowselectedData[0].companyName + '">' + rowselectedData[0].firstName + ' ' + rowselectedData[0].lastName + '</option>').appendTo(this);
        });
        customerInitSelect2(custometDropdownEles, {
            id: rowselectedData[0].customerId,
            text: rowselectedData[0].companyName + ' :: ' + rowselectedData[0].firstName + ' ' + rowselectedData[0].lastName
        });
        custometDropdownEles.val(rowselectedData[0].customerId).trigger("change");

        var reservePrice = rowselectedData[0].price;
        var minimumSellingPriceYen = rowselectedData[0].fob;
        var minSellingPriceInDollar = rowselectedData[0].minSellingPriceInDollar;
        $(reservePriceEditModalEle).find('input[name="reservePrice"]').autoNumeric('init').autoNumeric('set', reservePrice);
        $(reservePriceEditModalEle).find('input[name="minimumSellingPrice"]').val(minimumSellingPriceYen);
        $(reservePriceEditModalEle).find('input[name="minSellingPriceInDollar"]').val(minSellingPriceInDollar);
        $(reservePriceEditModalEle).find('select[name="currency"]').val(rowselectedData[0].currency).trigger('change');
        $(reservePriceEditModalEle).find('input.autonumber').autoNumeric('init').autoNumeric('update', {
            aSign: !(isEmpty(rowselectedData[0].currencySymbol)) ? rowselectedData[0].currencySymbol + ' ' : ""
        });

    }).on('hidden.bs.modal', function() {
        $(this).find('select,input,textarea').val('').trigger('change');
    }).on('change', 'select[name="custId"]', function() {

        var currencyEle = $('#currency')
        if (!isEmpty($(this).select2('data')[0])) {
            var data = $(this).select2('data')[0].data;
            currencyEle.val(!isEmpty(data) ? data.currencyType : '').trigger('change');
        } else {
            currencyEle.val('').trigger('change');
        }

    }).on('change', '#currency', function() {
        var data = $(this).select2('data')
        if (typeof data != undefined || data.length > 0) {
            data = data[0].data;
        }
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                $('#modal-edit-reserve-price').find('input.autonumber,span.autonumber').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                $('#modal-edit-reserve-price').find('input.autonumber,span.autonumber').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }

        }
    }).on('click', '#savePrice', function(event) {
        if (!$('#reseve-detail-form').valid()) {
            return;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        autoNumericSetRawValue($(reservePriceEditModalEle).find('.autonumber'));
        var data = getFormData($(reservePriceEditModalEle).find('input,select'));
        var reservePrice = getAutonumericValue($(reservePriceEditModalEle).find('input[name="reservePrice"]'));
        var minimumSellingPrice = data.currency == "1" ? getAutonumericValue($(reservePriceEditModalEle).find('input[name="minimumSellingPrice"]')) : getAutonumericValue($(reservePriceEditModalEle).find('input[name="minSellingPriceInDollar"]'));
        var a = Number(reservePrice)
        if (Number(reservePrice) < Number(minimumSellingPrice)) {
            alert($.i18n.prop('alert.reserved.price.notEqual'));
            return
        }
        var stock = rowselectedData[0].stockNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/reserve-details/update?stockNo=" + stock,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('input[name="accountFreeze"]').attr("value", "");
                    $('input[name="accountFreezeCustomer"]').attr("value", "");
                    table.ajax.reload();
                    $(reservePriceEditModalEle).modal('toggle');
                }

            }
        });

    })

    $('#modal-change-sales-person').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var ele = $('#modal-change-sales-person').find('#salesPersonId');
        $.getJSON(myContextPath + "/user/getRoleSales", function(data) {
            var salesJson = data;
            $(ele).select2({
                allowClear: true,
                width: '100%',
                data: $.map(salesJson, function(item) {
                    return {
                        id: item.userId,
                        text: item.username + ' ' + '( ' + item.userId + ' )'
                    };
                })
            }).val('').trigger("change");
        })
    }).on('hidden.bs.modal', function() {
        $(this).find('select').val('').trigger('change');
    }).on('click', '#btn-change-sales-person', function(event) {
        if (!$('#change-sales-person-form').valid()) {
            return;
        }
        let stockNos = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            stockNos.push(data.stockNo)
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(stockNos),
            url: myContextPath + "/sales/change/bidding-sales-person?salesPersonId=" + $('#modal-change-sales-person').find('#salesPersonId').find('option:selected').val(),
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-change-sales-person').modal('toggle');
                    table.ajax.reload();
                }

            }
        });
    })

    let NP_add_element = $('#add-ntfy-party')
    NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $(targetElement).closest('div.item-shipping').find('input#customerId').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-NP', function() {
        let isValid = $('#formAddNotifyParty').find('input[name="notifyPartyName"], input[name="notifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = NP_add_element.find('input[name="notifyPartyName"]').val();
        data['notifyPartyAddr'] = NP_add_element.find('input[name="notifyPartyAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addNotifyParty?code=" + data.code + "&notifyPartyName=" + data.notifyPartyName + "&notifyPartyAddr=" + data.notifyPartyAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-ntfy-party').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }

                                let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.npFirstName);
                                })

                                $('#modal-create-shipping').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-shipping').find('select[name="notifypartyId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(npArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }

                    });

                }
            }
        });
    })

    let consignee_add_element = $('#add-consigner')
    consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $(targetElement).closest('div.item-shipping').find('input#customerId').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-consig', function() {
        let isValid = $('#formAddConsignee').find('input[name="consigneeName"], input[name="consigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = consignee_add_element.find('input[name="consigneeName"]').val();
        data['consigneeAddr'] = consignee_add_element.find('input[name="consigneeAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addConsignee?code=" + data.code + "&consigneeName=" + data.consigneeName + "&consigneeAddr=" + data.consigneeAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-consigner').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }
                                let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.cFirstName);
                                })
                                $('#modal-create-shipping').find('select[name="consigneeId"]').empty();
                                $('#modal-create-shipping').find('select[name="consigneeId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(consArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }
                    });

                }
            }
        });
    })

    let Sales_NP_add_element = $('#add-sales-ntfy-party')
    Sales_NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = custdfdata.customerId;
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-sales-NP', function() {
        let isValid = $('#formSalesAddNotifyParty').find('input[name="salesNotifyPartyName"], input[name="salesNotifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = Sales_NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = Sales_NP_add_element.find('input[name="salesNotifyPartyName"]').val();
        data['notifyPartyAddr'] = Sales_NP_add_element.find('input[name="salesNotifyPartyAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addNotifyParty?code=" + data.code + "&notifyPartyName=" + data.notifyPartyName + "&notifyPartyAddr=" + data.notifyPartyAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-sales-ntfy-party').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }

                                let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.npFirstName);
                                })

                                $('#modal-create-sales').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-sales').find('select[name="notifypartyId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(npArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }

                    });

                }
            }
        });
    })

    let sales_consignee_add_element = $('#add-sales-consigner')
    sales_consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = custdfdata.customerId;
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-sales-consig', function() {
        let isValid = $('#formSalesAddConsignee').find('input[name="salesConsigneeName"], input[name="salesConsigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = sales_consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = sales_consignee_add_element.find('input[name="salesConsigneeName"]').val();
        data['consigneeAddr'] = sales_consignee_add_element.find('input[name="salesConsigneeAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addConsignee?code=" + data.code + "&consigneeName=" + data.consigneeName + "&consigneeAddr=" + data.consigneeAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-sales-consigner').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }
                                let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.cFirstName);
                                })
                                $('#modal-create-sales').find('select[name="consigneeId"]').empty();
                                $('#modal-create-sales').find('select[name="consigneeId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(consArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }
                    });

                }
            }
        });
    })

    let porfoma_NP_add_element = $('#add-porfoma-ntfy-party')
    porfoma_NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $('#modal-create-proforma').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-porfoma-NP', function() {
        let isValid = $('#formPorfomaAddNotifyParty').find('input[name="porfomaNotifyPartyName"], input[name="porfomaNotifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = porfoma_NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = porfoma_NP_add_element.find('input[name="porfomaNotifyPartyName"]').val();
        data['notifyPartyAddr'] = porfoma_NP_add_element.find('input[name="porfomaNotifyPartyAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addNotifyParty?code=" + data.code + "&notifyPartyName=" + data.notifyPartyName + "&notifyPartyAddr=" + data.notifyPartyAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-porfoma-ntfy-party').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }

                                let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.npFirstName);
                                })

                                $('#modal-create-proforma').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-proforma').find('select[name="notifypartyId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(npArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }

                    });

                }
            }
        });
    })

    let porfoma_consignee_add_element = $('#add-porfoma-consigner')
    porfoma_consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $('#modal-create-proforma').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-porfoma-consig', function() {
        let isValid = $('#formPorfomaAddConsignee').find('input[name="porfomaConsigneeName"], input[name="porfomaConsigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = porfoma_consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = porfoma_consignee_add_element.find('input[name="porfomaConsigneeName"]').val();
        data['consigneeAddr'] = porfoma_consignee_add_element.find('input[name="porfomaConsigneeAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addConsignee?code=" + data.code + "&consigneeName=" + data.consigneeName + "&consigneeAddr=" + data.consigneeAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-porfoma-consigner').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }
                                let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.cFirstName);
                                })
                                $('#modal-create-proforma').find('select[name="consigneeId"]').empty();
                                $('#modal-create-proforma').find('select[name="consigneeId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(consArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }
                    });

                }
            }
        });
    })

})

function findAllVessalsAndFwdrByOrginAndDestination(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        data: data,
        async: false,
        url: myContextPath + '/shipping/vessalsAndFwdr.json',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function customerInitSelect2(element, value) {
    $(element).select2({
        allowClear: true,
        minimumInputLength: 2,
        triggerChange: true,
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
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
                $(this.$element).empty();
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')',
                            data: item
                        });
                    });
                }
                return {
                    results: results
                }

            }

        },
        initSelection: function(element, callback) {
            callback({
                id: value.id,
                text: value.text,
                data: value
            });
        }
    });
}
function setDashboard(data) {
    $('#inquiry-count').html(data.data.inquiry);
    $('#porforma-count').html(data.data.porforma);
    $('#reserved-count').html(data.data.reserved);
    $('#shipping-count').html(data.data.shipping);
    $('#sales-count').html(data.data.salesorder);
    $('#status-count').html(data.data.status);
}
