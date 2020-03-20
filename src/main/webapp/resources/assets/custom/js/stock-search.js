var makersJson, supplierJson, modelList, CancelSelectedStock;

$(function() {
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    $('#filter-container').find('.autonumber').autoNumeric('init')
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('.autonumber').autoNumeric('init');
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
                    text: item.country
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        var categoryJson = data;
        var ele = $('#vehicleCategories');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',
            data: $.map(categoryJson, function(item) {
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
        });

    })
    $.getJSON(myContextPath + "/data/colors.json", function(data) {
        var colorsJson = data;
        var ele = $('#colors');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            data: $.map(colorsJson, function(item) {
                return {
                    id: item.color,
                    text: item.color
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/transmissionTypes.json", function(data) {
        var transmissionTypesJson = data;
        var ele = $('#transmissions');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            placeholder: function() {
                $(this).attr('data-placeholder');
            },
            width: '100%',
            data: $.map(transmissionTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        });
    })
    $('.select2').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#makers');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        });

    })
    $('#purchaseDate').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate.format('DD-MM-YYYY');
        purchased_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(purchased_min + ' - ' + purchased_max);
        $('#purchaseDateFrom').val(purchased_min);
        $('#purchaseDateTo').val(purchased_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))

    });
    $('#date-form-group').on('click', '.clear-date', function() {
        $('#purchaseDate').val('');
        $('#purchaseDateFrom').val('');
        $('#purchaseDateTo').val('');

        $(this).remove();

    })
    $('#makers').on('change', function() {
        var maker = $(this).val();
        var modelEle = $('#models');
        var modelList = [];
        var matchVal = 'item.name'
          , logic = '||'
          , condition = '';
        for (i = 0; i < maker.length; i++) {
            condition += matchVal + '==="' + maker[i] + '"||';
        }
        condition = condition.replace(/(.*?)([\|\|]+?)$/, '$1');
        $.each(makersJson, function(i, item) {
            if (eval(condition)) {
                $.merge(modelList, item.models);

            }

        });
        var prevSelectedValMaker = modelEle.val();
        if (typeof modelList == 'undefined' || modelList.length == 0) {

            return;
        }
        $(modelEle).empty();
        $(modelEle).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',
            data: $.map(modelList, function(item) {
                return {
                    id: item.modelName,
                    text: item.modelName,
                    data: item
                };
            })
        }).val(prevSelectedValMaker).trigger('change');

    });

    $('#models').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
    })
    $('#models').on('change', function() {
        var data = $(this).select2('data');
        var model = $(this).val();
        var subModelEle = $('#subModels');
        var subModelList = [];
        var matchVal = 'data.text'
          , logic = '||'
          , condition = '';
        if (!isEmpty(model)) {

            for (i = 0; i < model.length; i++) {
                condition += matchVal + '==="' + model[i] + '"||';
            }
        }
        condition = condition.replace(/(.*?)([\|\|]+?)$/, '$1');
        $.each(data, function(i, data) {
            if (eval(condition)) {
                $.merge(subModelList, ifNotValid(data.data.subModel, []));

            }

        });
        var prevSelectedVal = subModelEle.val();
        if (isEmpty(subModelList)) {

            return;
        }
        $(subModelEle).empty();
        $(subModelEle).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',

            data: $.map(subModelList, function(item) {
                return {
                    id: item.subModelName,
                    text: item.subModelName
                };
            })
        }).val(prevSelectedVal).trigger('change');

    });

    $('#subModels').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
    })
    $('#lotNos').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#grades').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#shifts').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        width: '100%'
    })
    $('#modelTypes').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#keywords').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#stockNos').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#auctionGrades').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    // Datatable
    var table = $('#table-stock').DataTable({

        "processing": true,
        "serverSide": true,
        "pageLength": 25,
        "searchDelay": 500,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",

        "ajax": {
            beforeSend: function() {
                $('div.stock-table>i.fa-spin').show()
            },
            complete: function() {
                $('div.stock-table>i.fa-spin').hide();
            },
            "url": myContextPath + "/shipping/stock-search/data",
            "data": function(data) {
                var criteria = $('#filter-container').find('input,select').serializeJSON();
                data['criteria'] = JSON.parse(criteria);
                data['criteria'].flag = $('input[type="radio"][name="statusFilter"]:checked').val();
                data['criteria'].account = $('#filter-account>option:selected').val();
                return data;

            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "order": [[2, "asc"]],
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
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
            //orderable: false,
            "searchable": true,
            "name": "stockNo",
            "data": "stockNo",
            "render": function(data, type, row) {
                return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + data + '">' + data + '</a>';
                //                 return '<a href="' + myContextPath + '/stock/details/' + data + '">' + data + '</a>';
            }

        }, {
            targets: 2,
            // orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            // orderable: false,
            "searchable": true,
            "data": "modelType",
            "name": "modelType"
        }, {
            targets: 4,
            // orderable: false,
            "className": "details-control",
            "searchable": true,
            "data": "maker",
            "name": "maker"
        }, {
            targets: 5,
            // orderable: false,
            "className": "details-control",
            "searchable": true,
            "data": "model",
            "name": "model"
        }, {
            targets: 6,
            "className": "details-control",
            // orderable: false,
            visible: true,
            "searchable": false,
            "data": "grade"

        }, {
            targets: 7,
            // orderable: false,
            "className": "dt-right details-control",
            "searchable": false,
            "data": "fob",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 8,
            //  orderable: false,
            "className": "details-control",
            "searchable": false,
            "data": "firstRegDate"

        }, {
            targets: 9,
            // orderable: false,
            visible: true,
            "className": "dt-right details-control",
            "searchable": false,
            "data": "buyingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 10,
            //orderable: false,
            "className": "details-control",
            visible: true,
            "searchable": false,
            "data": "purchaseInfo.sDate"

        }, {
            targets: 11,
            // orderable: false,
            "className": "details-control",
            visible: true,
            "searchable": false,
            "data": "equipment"

        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')
            $('#filter-container').find('input.autonumber').autoNumeric('init')
        }

    });
    // filter
    $('input[type="radio"][name="statusFilter"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        var btnReAuctionEle = $('#btn-reauction');
        //var btnCancelEle = $('#btn-cancel');

        if ($(this).val() == 0) {
            $(btnReAuctionEle).removeClass('hide');
            // $(btnCancelEle).removeClass('hide');
        } else if ($(this).val() == 1) {
            $(btnReAuctionEle).addClass('hide');
            // $(btnCancelEle).addClass('hide');
        } else if ($(this).val() == 2) {
            $(btnReAuctionEle).addClass('hide');
            // $(btnCancelEle).addClass('hide');
        }
        table.draw();
    })
    // supplier json
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;

    })

    $('#filter-account').on('change', function() {
        table.draw();
    })
    $('#modal-reauction').on('change', '#auctionCompany', function() {
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
    })
    // Re-Auction Modal
    // on open reserve modal
    $('#modal-reauction').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();

        }
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var selectedRowAuctionCompany = '';
            var selectedRowAuctionHouse = '';
            if (data.purchaseInfo.type == "auction") {
                selectedRowAuctionCompany = ifNotValid(data.purchaseInfo.supplier, '');
                selectedRowAuctionHouse = ifNotValid(data.purchaseInfo.auctionInfo.sAuctionHouse, '');
            }

            var element = $('#re-auction-clone-container>.item ').clone();
            $(element).appendTo($('#modal-reauction').find('.modal-body'));
            $(element).find('input[name="recycleAmount"]').val(ifNotValid(data.recycleAmount, ''));
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('#auctionCompany').select2({
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
            }).val(selectedRowAuctionCompany).trigger('change');
            $(element).find('#auctionHouse').select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                width: '100%'
            }).val(selectedRowAuctionHouse).trigger('change');

        });
    }).on('hidden.bs.modal', function() {
        $('#modal-reauction').find('.modal-body').html('')
    }).on('change', '#reaucDate', function(event) {

        var data = $(this).val();
        $('#modal-reauction').find('input.reauctionDate').val(data);

    }).on('click', '#save-reauction', function() {
        var data = [];
        var reAuctionDetails = $('#modal-reauction').find('.modal-body')
        var tr = reAuctionDetails.children(this)
        for (var i = 0; i < tr.length; i++) {
            var object;
            object = getFormData($(tr[i]).find('input[name="stockNo"],input[name="chassisNo"],input[name="reauctionDate"],select,input[name="recycleAmount"]'));
            data.push(object)
        }
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
            url: myContextPath + "/stock/re-auction/create",
            async: false,
            contentType: "application/json",
            success: function(data) {
                $('#modal-reauction').modal('toggle');
                table.ajax.reload()
            }
        });
    })

    $('#table-stock tbody').on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $('#btn-apply-filter').click(function() {
        autoNumericSetRawValue($('#filter-container').find('.autonumber'))
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        autoNumericSetRawValue($(selectedRows).find('span.autonumber'))
        table.draw();
    });
    $('#btn-reset-filter').click(function() {
        var elements = $('#filter-container').find('.autonumber');
        resetElementInput(elements);
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

    //modal cancel

    $('#modal-cancel').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        CancelSelectedStock = table.rows({
            selected: true,
            page: 'current'
        });
        if (CancelSelectedStock.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();

        }
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#add-remarks', function(event) {
        let remark = $('#cancel-form').find('textarea[name="vechicleRemarks"]').val();
        if (!$('#cancel-form').find('textarea[name="vechicleRemarks"]').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return false;
        }
        
        var data = [];
        var selectedObjects = CancelSelectedStock.data();
        for (var i = 0; i < selectedObjects.length; i++) {
            var obj = selectedObjects[i];
           
            //console.log(obj);
            data.push({"stockNo":obj.stockNo, "vehicleRemarks" : remark});
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/cancel/create",
            async: false,
            contentType: "application/json",
            success: function(data) {
                $('#modal-cancel').modal('toggle');
                table.ajax.reload()
            }
        });
    });
})
function format(rowData) {

    var html = '<div class="box-body no-padding bg-darkgray"><div class="table-responsive">' + '    <table class="table table-bordered" style="overflow-x:auto;">' + '        <tr>' + '            <th style="background-color: #9ab9de;">Auction Grade</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.grade != 'undefined' ? rowData.grade : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Year</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.firstRegDate != 'undefined' ? rowData.firstRegDate : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Transmission Type</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.transmission != 'undefined' ? rowData.transmission : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">No. of Door</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.noOfDoors != 'undefined' ? rowData.noOfDoors : '-') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th style="background-color: #9ab9de;">Fuel</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.fuel != 'undefined' ? rowData.fuel : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Driven</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.driven != 'undefined' ? rowData.driven : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Mileage</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.mileage != 'undefined' ? rowData.mileage + ' KM' : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Color</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.color != 'undefined' ? rowData.color : '') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th style="background-color: #9ab9de;">No. of Seat</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.noOfSeat != 'undefined' ? rowData.noOfSeat : '-') + '</span></td>' + '            <th style="background-color: #9ab9de;">Orgin</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.orgin != 'undefined' ? rowData.orgin : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">CC</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.cc != 'undefined' ? rowData.cc : '') + '</span></td>' + '            <th class="lable"></th>' + '            <td class="word-wrap value"><span></span></td>' + '    </table>' + '</div></div>';

    return html;
}
