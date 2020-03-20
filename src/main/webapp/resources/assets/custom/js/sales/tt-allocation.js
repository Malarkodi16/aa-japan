//own pop up dataTable hidden
$('#hidden-table').addClass('hidden');

$(function() {

    var table = $('#table-ttAllocation').DataTable({

        "dom": '<<t>ip>',
        "pageLength" : 25,
        "ajax": myContextPath + "/sales/tt-allocation-dataSource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "remitDate",

        }, {
            targets: 1,
            "data": "remitTypeName"
        }, {
            targets: 2,
            "data": "remitter"
        }, {
            targets: 3,
            "data": "bankName"
        }, {
            targets: 4,
            "data": "amount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 5,
            "data": "balanceAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 6,
            "render": function(data, type, row) {
                var html = ''
                html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#allocationModal" data-backdrop="static" data-keyboard="false">Own</button>'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-ttAllocation span.autonumber').autoNumeric('init')
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

     $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

var date;
    $('#table-filter-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        date = '';
        $('#table-filter-date').val('');
        $(this).remove();
        table.draw();

    })

        $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Due Date Filter
        if (typeof date != 'undefined' && date.length != '') {
            if (aData[0].length == 0 || aData[0] != date) {
                return false;
            }
        }

        return true;
    });


    /* Customer Id Select2 Filter */

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

    var tableUnitAllocation = $('#Unit-Allocation-details-table').DataTable({
    	"pageLength" : 25,
        "ajax": {
            url: myContextPath + "/sales/tt-unitAllocation-dataSource-list",
            data: function(data) {
                data.customer = $('#allocationModal').find('select[name="custId"]').val();
            },
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

            "data": "stockNo",
            "render": function(data, type, row) {
                return '<input type="hidden" class="form-control" name="id"  value="' + row.id + '"/>' + data
            }

        }, {
            targets: 1,

            "data": "chassisNo",

        }, {
            targets: 2,

            "data": "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 3,
            "data": "received",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 4,

            "data": "balance",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 5,

            "render": function(data, type, row) {
                var html = ''

                html += '<input type="text" class="form-control autonumber priceAllocation" data-v-min="0" data-a-sign="' + row.currencySymbol + ' " name="priceAllocation" />'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#Unit-Allocation-details-table span.autonumber,input.autonumber').autoNumeric('init')
        }

    })

    // expand details
    $('#allocationModal').find('select[name="custId"],select[name="allocationType"]').on('change', function() {
        var allocationValue = $("#allocationType option:selected").text();
        var customer = $('#allocationModal').find('select[name="custId"]').val();
        if ((customer != "" || customer == null) && allocationValue == "Unit Allocation") {
            $('#hidden-table').removeClass('hidden');
            tableUnitAllocation.ajax.reload()
        } else {
            $('#hidden-table').addClass('hidden');
        }

    });

    $('#allocationModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var tr = triggerElement.closest('tr');
        var data = table.row(tr).data();
        var amount = ifNotValid(data.balanceAmount, '');
        var id = ifNotValid(data.daybookId, '');
        var itemElement = $('#allocationModal').closest('div');
        $(itemElement).find('input[name="amount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' '
        });
        $(itemElement).find('input[name="amount"]#amount').val(amount);
        $(itemElement).find('input[name="refId"]').val(id);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('.select2-hidden-accessible').val('').trigger('change');
        $('#hidden-table').addClass('hidden');
    });

    $("#save-ttAllocation").on('click', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var object;
        var transactionDetails = $('#allocationModal')
        object = getFormData(transactionDetails.find('input,select'));
        var queryString = "?customerId=" + object.custId + "&allocationType=" + object.allocationType + "&amount=" + object.amount + "&daybookId=" + object.refId;
        if (object.allocationType == 2) {
            var dataArr = [];
            tableUnitAllocation.rows().every(function(rowIdx, tableLoop, rowLoop) {
                var tr = tableUnitAllocation.row(this).node();
                var data = {
                    'id': $(tr).find('input[name="id"]').val(),
                    'priceAllocation': getAutonumericValue($(tr).find('input[name="priceAllocation"]'))
                }
                dataArr.push(data);
            })

        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "POST",
            async: false,
            data: JSON.stringify(dataArr),
            url: myContextPath + "/sales/tt-create" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status == "success") {
                    $('#allocationModal').modal('toggle');
                    table.ajax.reload();
                } else {
                    alert(data.message);
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    })
})
