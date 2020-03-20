$(function() {
	
	var tableEle = $('#manufacture-table')
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/data/ship/charge.json",
       
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        },{
            targets: 0,
            "data": "originCountry"
        }, {
            targets: 1,
            "data": "destCountry"
        }, {
            targets: 2,
            "data": "m3From"
        }, {
            targets: 3,
            "data": "m3To"
        }, {
            targets: 4,
            "className": "dt-right",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
           
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')

        }
        
    })
     function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    
    //Global Search
    $('#manufacture-table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    //Table Length Filter
    $('#manufacture-table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //create Year of manufacture
    $('#create-ship-charge').on('')
})
