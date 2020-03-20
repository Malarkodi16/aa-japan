var supplierJson;
$(function() {

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
    //maker dropdown
    var makerFilterElement = $('#maker-filter');
    var modelFilterElement = $('#model-filter');
    $.getJSON(myContextPath + "/data/makers.json", function(data) {
        $(makerFilterElement).select2({
            allowClear: true,
            data: $.map(data, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        });

    })
    makerFilterElement.on('change', function() {
        var data = makerFilterElement.select2('data')[0].data;
        modelFilterElement.empty();
        if (isEmpty(data)) {
            return;
        }
        $(modelFilterElement).select2({
            allowClear: true,
            data: $.map(data.models, function(item) {
                return {
                    id: item,
                    text: item
                };
            })
        }).val('').trigger('change');
    })
    //maker element

    $(modelFilterElement).select2({
        allowClear: true
    });
    // Date range picker
    var invoiceDateMin;
    var invoiceDateMax;
    $('#table-filter-invoice-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        invoiceDateMin = picker.startDate.format('DD-MM-YYYY') ;
        invoiceDateMax = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(invoiceDateMin+ ' - ' +invoiceDateMax );
        $('#date-form-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
      
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        invoiceDateMin = '';
        invoiceDateMax = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();

    })
    // Datatable
    var table = $('#table-report').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/stockSales/data-source",
            data: function(data) {
                data.fromDate =invoiceDateMin;
                data.toDate = invoiceDateMax;
                data.maker = $('#maker-filter').val();
                data.model = $('#model-filter').val();
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "data": "stockNo",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 1,
            "data": "createdDate"
        }, {
            targets: 2,
            "className" : "dt-right",
            "data": "purchaseCost",
            "width": "100px",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 3,
            "className" : "dt-right",
            "data": "commision",
            "width": "100px",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "className" : "dt-right",
            "data": "otherCharges",
            "width": "100px",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className" : "dt-right",
            "data": "pTotal",
            "width": "100px",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                return '<span name="pTotal" class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className" : "dt-right",
            "data": "sTotal",
            "width": "100px",
            render: function(data, type, row) {
                data = data == null ? '' : data;
                return '<span name="sTotal" class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            $('#table-report').find('.autonumber').autoNumeric('init')

        }

    });
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('#btn-search').on('click',function(){
        table.ajax.reload();
    })
    

})

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }

    // total Amount Outstanding
    var purchaseInfoTotal = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="pTotal"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    
    //total Current Amount
    var salesInfoTotal = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="sTotal"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
    $('#table-report>tfoot>tr.totalFooter').find('span.purchaseInfoTotal').autoNumeric('init').autoNumeric('set', purchaseInfoTotal);

    $('#table-report>tfoot>tr.totalFooter').find('span.salesInfoTotal').autoNumeric('init').autoNumeric('set', salesInfoTotal);

}
