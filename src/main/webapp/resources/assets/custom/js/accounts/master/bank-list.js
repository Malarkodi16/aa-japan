$(function() {

    var tableEle = $('#table-master-bank-list')
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/master/list-bank-data-source",
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
            "data": "bankName",

        }, {
            targets: 1,
            "data": "coaCode"
        }, {
            targets: 2,
            "className": "dt-right",
            "data": "yenBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<div>' + row.currency + ' ' + '<span class="amount" id="yenBalance" data-m-dec="0">' + data + '</span></div>';
            }
        }, {
            targets: 3,
            "className": "dt-right",
            "data": "clearingBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (!isEmpty(data)) {
                    return '<div>' + row.currency + ' ' + '<span class="amount" id="clearingBalance" data-m-dec="0">' + data + '</span></div>';
                }
            }
        }, {
            targets: 4,
            "visible": false,
            "data": "bankSeq"
        }]
    })

    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    	
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
})
