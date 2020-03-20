var shippingInstructionId;
var table
$(function() {
    shippingInstructionId = $('#shipping-arranged-id').val()
    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#inquiry-count').html(data.data.inquiry);
        $('#porforma-count').html(data.data.porforma);
        $('#reserved-count').html(data.data.reserved);
        $('#shipping-count').html(data.data.shipping);
        $('#sales-count').html(data.data.salesorder);
        $('#status-count').html(data.data.status);
    });

    var isAdminManagerValue = $('input[name="isAdminManager"]').val();
    isAdminManagerValue =  (isAdminManagerValue == 1) ? true : false ;
    // Datatable
    table = $('#table-shipping-instruction').DataTable({
        "pageLength": 25,
        "ordering": false,
        "ajax": myContextPath + "/sales/shipping-instruction-data",
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
            "visible": false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "date",
            "data": "date",

        }, {
            targets: 2,
            orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "stockNo",
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 3,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 4,
            "className": "details-control",
            orderable: false,
            "searchable": true,
            "data": "customerFN",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var customerLN = row.customerLN == null ? '' : row.customerLN;
                return data + ' ' + customerLN;
            }
        }, {
            targets: 5,
            orderable: false,
            "searchable": true,
            "data": "consigneeFN"
        }, {
            targets: 6,
            orderable: false,
            "searchable": true,
            "data": "notifyPartyFN"
        },  {
            targets: 7,
            orderable: false,
            
            "data": "instructedBy",
            "visible" : isAdminManagerValue
            
        },{
            targets: 8,
            orderable: false,
            "searchable": false,
            "data": "destCountry"
        }, {
            targets: 9,
            orderable: false,
            "searchable": true,
            "data": "destPort"
        }, {
            targets: 10,
            orderable: false,
            "searchable": true,
            "data": "chassisNo",
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="#" class="ml-5 btn btn-info btn-xs" title="Update User Details" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-shipping-user-details"><i class="fa fa-edit"></i> User Details</a>'
                return html;
            }
        }]

    });

    var userName = $('#userInfo').text();
    $('#showMine').change( function() {
  	  if ($(this).is(':checked')) {
  	    $.fn.dataTable.ext.search.push(
  	      function(settings, aData, dataIndex) {  
  	         return aData[7].toLowerCase()  === userName.toLowerCase() 
  	      }
  	    )
  	  } else {
  	    $.fn.dataTable.ext.search.pop()
  	  }
  	  table.draw()
  	})
    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-shipping-date').daterangepicker({
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
        $('#table-filter-shipping-date').val('');
        $(this).remove();

    })
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == 'table-shipping-instruction') {
            //id filter for notification
            if (!isEmpty(shippingInstructionId)) {
                if (aData[0].length == 0 || aData[0] != shippingInstructionId) {
                    return false;
                }
            }
        }
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
        return true;
    });
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
    })

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
