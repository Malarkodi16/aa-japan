$(function() {
	// Date picker
    $('.datepicker').datepicker({
    	format: "dd-mm-yyyy",
        autoclose: true
    })
   var table= $('#table-exchangeRate-list').DataTable({
		"ajax" :{
			"url":myContextPath + "/exchange/findByDate",
			"data": function(data){
				data.date =$('#exchangeRateDate').val() ;
		       
		      }
		}, 
		"pageLength" : 25,
		columnDefs : [ {
			"targets" : '_all',
			"defaultContent" : ""
		}, {
			orderable : false,
			className : 'select-checkbox',
			targets : 0
		}, {
			targets : 0,
			orderable : false,
			className : 'select-checkbox',
			"data" : "id",
			"render" : function(data, type, row) {
				data = data == null ? '' : data;
				if (type === 'display') {
					return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
				}
				return data;
			}
		}, {
			targets : 1,
			"data" : "dollarRate",
			
		}, {
			targets : 2,
			"data" : "audRate"
		},  {
			targets : 3,
			"data" : "poundRate"
		}
		]
	
	});
    $('#btn-search').on('click',function(){
    	table.ajax.reload()
    })
    
})