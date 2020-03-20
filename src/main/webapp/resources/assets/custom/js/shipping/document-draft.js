$(function() {

    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $.getJSON(myContextPath + "/shipping/bl/document-dashboard/status-count", function(data) {
        setDocumentDashboardCount(data.data)
    });
    //reserve customer server side search 

    $('#custId').select2({
        allowClear: true,
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
    });

    var table = $('#table-document-draft-list').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/shipping/bl/draft-list",
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
            "data": "fName"
        }, {
            targets: 3,
            "data": "vesselId"
        }, {
            targets: 4,
            "data": "orginCountry"
        }, {
            targets: 5,
            "data": "orginPort"
        }, {
            targets: 6,
            "data": "destCountry"
        }, {
            targets: 7,
            "data": "destPort"
        }, {
            targets: 8,
            "data": "sEtd"
        }, {
            targets: 9,
            "data": "sEta"
        }, {
            targets: 10,
            "data": "blDraftStatus",
            "className": 'align-center',
            "render": function(data, type, row) {
                var blDraftStatus;
                var className;
                if (data == 0) {
                    blDraftStatus = "Not Recieved"
                    className = "default"
                } else if (data == 1) {
                    blDraftStatus = "Recieved"
                    className = "success"
                } else {
                    blDraftStatus = "-"
                    className = "default"
                }
                return '<span class="label label-' + className + '">' + blDraftStatus + '</span>';
            }

        }, {
            targets: 11,
            "data": "customerId",
            "visible": false
        }]

    });
    //Draft Table Checkbox Property
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

    //Table Global Search
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();

    });
    //Table Length
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    //Filter with Customer
    var fName;
    $('.customer').change(function() {
        fName = $(this).val();
        table.draw();

    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Customer filter

        if (typeof fName != 'undefined' && fName.length != '') {
            if (aData[11].length == 0 || aData[11] != fName) {
                return false;
            }
        }
        return true;
    })

    //Bl Status
    $('.status').select2({
        allowClear: true,
        width: "100%"
    })

    //Draft Status Update
    var data_stock = [];
    $('#update-status').on('click', function() {
        if (!confirm('Do you want to save?')) {
            return false;
        }
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            data_stock.push(data.id);
        })
        var data = {};
        var statusCheckVal = $('#bl-status').find('select').val();
        if (statusCheckVal == "") {
            alert($.i18n.prop('alert.please.select.status'))
        } else {

            data["status"] = statusCheckVal;

            data["status"] = $('#bl-status').find('select').val();
            data["ids"] = data_stock;
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "put",
                data: JSON.stringify(data),
                url: myContextPath + "/shipping/bl/update-draft",
                contentType: "application/json",
                success: function(status) {
                    table.ajax.reload();
                    $('#bl-status').find('select').val('').trigger('change');
                }
            })
        }
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
