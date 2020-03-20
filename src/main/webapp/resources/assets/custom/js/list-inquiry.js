var categoryJson, makersJson, vechicleCategories, vechicleCountryPort, vechicleMakersModel;
$(function() {
     $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    //Setting count in dashboard
    setDashboardStatusCount()

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        var countriesJson = data;
        $('.country-dropdown').select2({
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
        $('#orgin').val($('#orgin').attr('data-value')).trigger("change");
    })

    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('.maker-dropdown');
        $(ele).select2({
            width: '100%',
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger("change");

    })
    $('#makers').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#models');
        $(modelEle).empty();
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }
        table.draw();
    });

    $('#models').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    }).on('change', function() {
        var model = $(this).val();
        table.draw();
    });

    // Datatable
    var table = $('#table-inquirylist').DataTable({

        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": myContextPath + "/inquiry/inquirylist-data",
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
            "searchable": false,
            "visible": false,
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
            "className": "details-control",
            "visible": true,
            "searchable": true,
            "name": "createdDate",
            "data": "createdDate"
        }, {
            targets: 2,
            //orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "customerName",
            "data": "customerName",

        }, {
            targets: 3,
           // orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "mobile",
            "data": "mobile"
        }, {
            targets: 4,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "skypeId",
            "data": "skypeId"
        }, {
            targets: 5,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "companyName",
            "data": "companyName"
        }, {
            targets: 6,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "category",
            "data": "category"
        }, {
            targets: 7,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "subCategory",
            "data": "subCategory"
        }, {
            targets: 8,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "maker",
            "data": "maker"
        }, {
            targets: 9,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "model",
            "data": "model"
        }, {
            targets: 10,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "country",
            "data": "country"
        }, {
            targets: 11,
           // orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "port",
            "data": "port"
        }, {
            targets: 12,
            orderable: false,
            "className": "details-control align-center",
            "searchable": true,
            "name": "id",
            "data": "id",
            "render": function(data, type, row) {

                var searchUrl = myContextPath + '/sales/stock/stock-search?category=' + row.subCategory + '&maker=' + row.maker + '&model=' + row.model
                var html;
                html = '<a type="button" class="btn btn-primary btn-xs edit" data-backdrop="static" data-keyboard="false"data-toggle="modal" data-target="#myModalInquiryList" title="Edit" id="cancel-purchase"><i class="fa fa-fw fa-edit"></i></a>';

                html += '<a type="button" class="btn btn-danger ml-5 btn-xs delete" title="Delete" id="delete-btn"><i class="fa fa-fw fa-trash"></i></a>'
                html += '<a type="button" href="' + searchUrl + '" class="btn btn-default ml-5 btn-xs search-btn" title="Search"><i class="fa fa-fw fa-search"></i></a>'
                return html;
            }
        }, {
            targets: 13,
           // orderable: false,
            "className": "details-control",
            "searchable": true,
            "data": "subModel",
            "visible":false
        }]

    });

    var purchased_min;
    var purchased_max;
    $('#table-filter-inquiry-date').daterangepicker({
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
        $('#table-filter-inquiry-date').val('');
        $(this).remove();

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        var filterMaker = $('#makers').find('option:selected').val();
        var filterModel = $('#models').find('option:selected').val();
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
        if (typeof filterMaker != 'undefined' && filterMaker.length != '') {
            if (aData[8].length == 0 || aData[8] != filterMaker) {
                return false;
            }
        }
        // Supplier filter
        if (typeof filterModel != 'undefined' && filterModel.length != '') {
            if (aData[9].length == 0 || aData[9] != filterModel) {
                return false;
            }
        }
        return true;
    });

    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\./gi, "").replace(/\s+/g, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
    	var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //myModalInquiryList edit
    var upele;
    var editRow;
    $('#myModalInquiryList').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        upele = $(event.relatedTarget);
        editRow = $(event.relatedTarget).closest("tr");
        var rowData = table.row(editRow).data();
        console.log(rowData)
        /* to Save data after editing*/
        $(this).find('input[name="inquiryId"]').val(rowData.id).trigger("change");
        $(this).find('input[name="inquiryItemId"]').val(rowData.inquiryVehicleId).trigger("change");
        var categoryAndSubcategory = rowData.category + ">" + rowData.subCategory
        $(this).find('input[name="categoryAndSubcategory"]').val(categoryAndSubcategory);
        $(this).find('input[name="category"]').val(rowData.category);
        $(this).find('input[name="subCategory"]').val(rowData.subCategory);
        $(this).find('select[name="maker"]').val(rowData.maker).trigger("change");
        $(this).find('select[name="model"]').val(rowData.model).trigger("change");
        $(this).find('select[name="subModel"]').val(rowData.subModel).trigger("change");
        $(this).find('select[name="country"]').val(rowData.country).trigger("change");
        $(this).find('select[name="port"]').val(rowData.port).trigger("change");
    }).on('click', '#btn-edit', function(event) {
        if(!$('form#inquiry-edit-modal').valid()){
             return ;
        }
        if(!confirm($.i18n.prop('common.confirm.save'))){
            return false;
        }
        var object;
        var data = {};
        data["inquiryId"] = $(this).closest('.modal-body').find("input[name='inquiryId']").val();
        data["inquiryItemId"] = $(this).closest('.modal-body').find("input[name='inquiryItemId']").val();
        $(this).closest('.modal-body').find('.row').each(function() {
            object = getFormData($(this).closest('.modal-body').find('input,select,textarea'));

        });

        data['inquiryData'] = object;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/inquiry/update",
            contentType: "application/json",
            async: false,
            success: function(data) {
                var tr = $(upele).closest('tr');
                var row = table.row(tr);
                //                 table.ajax.reload();
                row.data(data.data).invalidate();
                $('#myModalInquiryList').modal('toggle');

            }
        });
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on('change', '#maker', function() {
        var maker = $(this).val();
        var modelEle = $('#myModalInquiryList').find('#model');
        modelEle.empty().trigger('change');
        var modelList = [];
        var data = $(this).select2('data');
        if (!isEmpty(data) && data.length > 0 && !isEmpty(data[0].data)) {
            $(modelEle).select2({
                width: '100%',
                allowClear: true,
                data: $.map(data[0].data.models, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName,
                        data: item

                    };
                })

            }).val('').trigger('change');
        }

    }).on('change', '#model', function() {
        var data = $(this).select2('data');
        var categorieEle = $('#myModalInquiryList').find('#category');
        var subcategorieEle = $('#myModalInquiryList').find('#subCategory');
        var categoryAndSubcategoryEle = $('#myModalInquiryList').find('#categoryAndSubcategory');
        var subModelEle = $('#myModalInquiryList').find('#subModel');
        subModelEle.empty();
       categorieEle.val('');
       subcategorieEle.val('');
       categoryAndSubcategoryEle.val('');
       
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var subModelList = data[0].data.subModel;
            var catg = ifNotValid(data[0].data.category, '');
            var subcatg = ifNotValid(data[0].data.subcategory, '');
             $(subModelEle).select2({
                allowClear: true,
                data: $.map(subModelList, function(item) {
                    return {
                        id: item.subModelName,
                        text: item.subModelName
                    };
                })
            }).val('').trigger("change");
            categorieEle.val(catg)
            subcategorieEle.val(subcatg);
            categoryAndSubcategoryEle.val(catg + '->' + subcatg)

        }
    }).on('change', 'select[name="country"]', function() {
        var country = $(this).val();
        var portEle = $('#myModalInquiryList').find('select[name="port"]');
        portEle.empty();
        var portEleList = [];
        var data = $(this).select2('data');
        if (!isEmpty(data) && data.length > 0 && !isEmpty(data[0].data)) {
            $(portEle).select2({
                width: '100%',
                allowClear: true,
                data: $.map(data[0].data.port, function(item) {
                    return {
                        id: item,
                        text: item

                    };
                })

            }).val('').trigger('change');
        }
    })

    $('#myModalInquiryList').find('select[name="port"]').select2({
        allowClear: true,
        width: '100%',
        placeholder: 'Search Destination Port',
    })
    $('#myModalInquiryList').find('select[name="model"]').select2({
        allowClear: true,
        width: '100%',
        placeholder: 'Search model ',
    })

    $('#table-inquirylist').on('click', '.delete', function() {

        var data = {};
        var container = table.row($(this).closest('tr')).data();
        data["id1"] = container.id;
        data["inquiryId"] = container.inquiryVehicleId;
        /* $(this).closest("tr").remove();*/
        var len = $(this).closest('tr').length;
        if (len == 1) {
            var checkstr = confirm($.i18n.prop('common.confirm.delete'));
            if (checkstr == true) {
                $(this).closest("tr").remove();
            } else {
                return false;
            }
        }
        var len = $(this).closest('row').length;
        if (len > 1) {
            var checkstr = confirm($.i18n.prop('common.confirm.delete'));
            if (checkstr == true) {
                $(this).closest("tr").remove();
            } else {
                return false;
            }
        }
        var tr = $(this).closest('tr');
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/inquiry/delete",
            contentType: "application/json",
            async: false,
            success: function(data) {
                //reset count
                setDashboardStatusCount();
                //remove row from table
                var row = table.row(tr);
                row.remove().draw();

                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Delete Success!</strong> Inquiry is Deleted Successfully');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
            }
        });

    });

})
function setDashboardStatusCount() {
    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#inquiry-count').html(data.data.inquiry);
        $('#porforma-count').html(data.data.porforma);
        $('#reserved-count').html(data.data.reserved);
        $('#shipping-count').html(data.data.shipping);
        $('#sales-count').html(data.data.salesorder);
        $('#status-count').html(data.data.status);

    });
}
