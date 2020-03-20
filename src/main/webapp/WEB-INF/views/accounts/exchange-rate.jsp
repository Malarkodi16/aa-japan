<sec:authorize access="hasRole('ADMIN')" var="isAdminOrNot"></sec:authorize>
<div class="modal fade" id="exchangerate-modal">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Exchange Rates</h4>
			</div>
			<div class="modal-body">
				<table id="table-exchange-rate"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th></th>
							<th style="text-align: center;">Exchange Rate</th>
							<th style="text-align: center;">Sales Exchange Rate</th>
							<th style="text-align: center;">Special Exchange Rate</th>
						</tr>
					</thead>
					<tbody>
						<tr class="us">
							<th>US Dollar</th>
							<td><input type="hidden" class="form-control autonumeric us"
								name="currency" value="2"><input type="text"
								class="form-control autonumeric us" data-a-sign="$ "
								data-v-min="0" data-m-dec="2" name="exchangeRate"
								id="currency_exchange_rate_2" placeholder="$ Exchange Rate"></td>
							<td><input type="text" class="form-control autonumeric us"
								data-a-sign="$ " data-v-min="0" data-m-dec="2"
								name="salesExchangeRate" id="currency_sales_exchange_rate_2"
								placeholder="$ Sales Rate"></td>
							<td><input type="text" class="form-control autonumeric us"
								data-a-sign="$ " data-v-min="0" data-m-dec="2"
								name="specialExchangeRate"
								id="currency_special_exchange_rate_2"
								placeholder="$ Special Rate"></td>
						</tr>
						<tr class="american">
							<th>Australian Dollar</th>
							<td><input type="hidden"
								class="form-control autonumeric american" name="currency"
								id="currency_3" value="3"><input type="text"
								class="form-control autonumeric american" data-a-sign="A$ "
								data-v-min="0" data-m-dec="2" name="exchangeRate"
								id="currency_exchange_rate_3" placeholder="A$ Exchange Rate"></td>
							<td><input type="text"
								class="form-control autonumeric american" data-a-sign="A$ "
								data-v-min="0" data-m-dec="2" name="salesExchangeRate"
								id="currency_sales_exchange_rate_3"
								placeholder="A$ Sales Rate"></td>
							<td><input type="text"
								class="form-control autonumeric american" data-a-sign="A$ "
								data-v-min="0" data-m-dec="2" name="specialExchangeRate"
								id="currency_special_exchange_rate_3"
								placeholder="A$ Special Rate"></td>
						</tr>
						<tr class="pound">
							<th>Pound Rate</th>
							<td><input type="hidden"
								class="form-control autonumeric pound" name="currency"
								id="currency_4" value="4"><input type="text"
								class="form-control autonumeric pound" data-a-sign="£ "
								data-v-min="0" data-m-dec="2" name="exchangeRate"
								id="currency_exchange_rate_4" placeholder="£ Exchange Rate"></td>
							<td><input type="text"
								class="form-control autonumeric pound" data-a-sign="£ "
								data-v-min="0" data-m-dec="2" name="salesExchangeRate"
								id="currency_sales_exchange_rate_4" placeholder="£ Sales Rate"></td>
							<td><input type="text"
								class="form-control autonumeric pound" data-a-sign="£ "
								data-v-min="0" data-m-dec="2" name="specialExchangeRate"
								id="currency_special_exchange_rate_4"
								placeholder="£ Special Rate"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<div class="btn-toolbar pull-right">
					<button type="button" class="btn btn-primary" id="saveExchg"
						name="saveExchg">Save</button>
					<button type="button" class="btn btn-primary"
						data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
