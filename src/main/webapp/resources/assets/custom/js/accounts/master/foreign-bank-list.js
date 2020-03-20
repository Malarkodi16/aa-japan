var countryJson;
$(function() {

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        $('select[name="country"]').select2({
            placeholder: "Select Country",
            allowClear: true,
            width: '100%',
            data: $.map(countryJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        });
    })

    var tableEle = $('#table-master-bank-list')
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/list-foreignbank-data",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "width": "100px",
            "data": "bank",

        }, {
            targets: 1,
            "data": "country"
        }, {
            targets: 2,
            "className": "dt-right",
            "data": "beneficiaryCertify"

        }, {
            targets: 3,
            "className": "dt-right",
            "data": "licenseDoc"

        }, {
            targets: 4,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Bank" id="edit-bank" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-bank" name="editBank" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }

        }]
    })

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;$('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });

    var modelEditEle = $('#modal-edit-bank');
    var editRow, modalEditTriggerEle, modelList;
    modelEditEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalEditTriggerEle = $(event.relatedTarget);
        var tr = $(modalEditTriggerEle).closest('tr');
        row = table.row(tr)
        var data = row.data();
        // data to check duplicate sub category
        $(modelEditEle).find('input[name="bankId"]').val(data.bankId)
        $(modelEditEle).find('input[name="bank"]').val(data.bank)
        $(modelEditEle).find('textarea[name="beneficiaryCertify"]').val(data.beneficiaryCertify)
        $(modelEditEle).find('textarea[name="licenseDoc"]').val(data.licenseDoc)
        $(modelEditEle).find('select[name="country"]').val(data.country).trigger('change')
    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
        $(this).find('span.help-block').html('');
        $(this).find('.has-error').removeClass('has-error');
    }).on('click', '#bank-edit', function() {

        if (!$('#modal-edit-bank-form').find('input,textarea,select').valid()) {
            return false;
        }
        var model = getFormData($(modelEditEle).find('input,select,textarea'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            async: false,
            data: JSON.stringify(model),
            url: myContextPath + "/master/edit-bank",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $(modelEditEle).modal('toggle');
                    table.ajax.reload();

                }
            }
        });

    })

    $('#table-master-bank-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table.row(tr).data();
        var bankId = data.bankId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/bank/delete/" + bankId,
            contentType: "application/json",
            success: function(data) {
                table.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Bank is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
