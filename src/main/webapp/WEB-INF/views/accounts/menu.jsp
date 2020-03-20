<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu" data-widget="tree">
			<sec:authorize access="canAccess(1)">
				<li><a href="${contextPath }/accounts/dash-board/view"><i
						class="fa fa-dashboard"></i> <span>Dashboard</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(2)">
				<li class="treeview"><a href="#"> <i
						class="fa fa-file-pdf-o"></i> <span>LC Management</span> <span
						class="pull-right-container"> <i
							class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(3)">
							<li><a href="${contextPath }/accounts/lc/create"><i
									class="fa fa-circle-o"></i>Create</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(4)">
							<li><a href="${contextPath }/accounts/lc/list"><i
									class="fa fa-circle-o"></i>List</a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="canAccess(5)">
				<li class="treeview"><a href="#"> <i
						class="fa fa-file-text"></i> <span>Invoice Booking &
							Approval</span> <span class="pull-right-container"> <i
							class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(6)">
							<li><a
								href="${contextPath }/accounts/invoice/booking/auction"><i
									class="fa fa-circle-o"></i>Booking</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(7)">
							<li><a
								href="${contextPath }/accounts/invoice/approval/auction"><i
									class="fa fa-circle-o"></i>Approval</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(8)">
							<li><a
								href="${contextPath }/accounts/payment/invoice/reauction"><i
									class="fa fa-circle-o"></i>Re Auction</a></li>
						</sec:authorize>
						<li><a
							href="${contextPath }/accounts/payment/invoice/transport/mismatch"><i
								class="fa fa-circle-o"></i>Transport Approval</a></li>
			</sec:authorize>
			<li><a href="${contextPath }/accounts/cancelledInvoices/list"><i
					class="fa fa-circle-o"></i>Cancelled Invoices</a></li>
		</ul>
		</li>
		<sec:authorize access="canAccess(9)">
			<li class="treeview"><a href="#"> <i class="fa fa-paypal"></i>
					<span>Payment</span> <span class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(10)">
						<li><a href="${contextPath }/accounts/approve-payment"><i
								class="fa fa-circle-o"></i>Payment Processing</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(11)">
						<li><a href="${contextPath }/accounts/auction-payment-done"><i
								class="fa fa-circle-o"></i>Payment Approval</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(12)">
						<li><a
							href="${contextPath }/accounts/auction-payment-freezed"><i
								class="fa fa-circle-o"></i>Payment Completed</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(13)">
						<li><a href="${contextPath }/accounts/payment/tracking"><i
								class="fa fa-circle-o"></i>Payment Tracking</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(14)">
			<li><a href="${contextPath }/accounts/payment/advance"><i
					class="fa fa-money"></i><span>Advance & Prepayments</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(15)">
			<li class="treeview"><a href="#"> <i class="fa fa-file-text"></i>
					<span>Receipts</span> <span class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(16)">
						<li><a href="${contextPath }/exchange/list-page"><i
								class="fa fa-circle-o"></i>Exchange Rate List</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(17)">
						<li><a href="${contextPath }/accounts/daybook-entry"><i
								class="fa fa-circle-o"></i>Receipts Booking</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(18)">

						<li><a href="${contextPath }/accounts/daybook-list"><i
								class="fa fa-circle-o"></i>Receipts List</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(19)">
						<li><a href="${contextPath }/daybook/approve"><i
								class="fa fa-circle-o"></i>Receipts Approval</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(20)">
						<li><a href="${contextPath }/daybook/tt-allocation"><i
								class="fa fa-circle-o"></i>TT Allocation List</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(21)">
			<li><a href="${contextPath }/accounts/report/journalEntry"><i
					class="fa fa-edit"></i><span>Journal Entry</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(22)">
			<li><a href="${contextPath }/accounts/forward/booking/view"><i
					class="fa fa-forward"></i><span>Forward Booking</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(23)">
			<li><a href="${contextPath }/accounts/customer/approve/list"><i
					class="fa fa-check-square"></i><span>Customer Approve List</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(24)">
			<li class="treeview"><a href="#"> <i class="fa fa-database"></i>
					<span>Master Data</span> <span class="pull-right-container">
						<i class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(25)">
						<li><a href="${contextPath }/accounts/coa-list"><i
								class="fa fa-circle-o"></i>Chart Of Account List</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(26)">
						<li><a href="${contextPath }/master/list-bank"><i
								class="fa fa-circle-o"></i>Bank</a></li>
					</sec:authorize>
					<!-- Added by Yogeshwar -->

					<li><a href="${contextPath }/master/foreign-bank-list"><i
							class="fa fa-circle-o"></i>Foreign Bank</a></li>

					<!-- Added by Yogeshwar -->
					<sec:authorize access="canAccess(27)">
						<li><a href="${contextPath }/accounts/master/shipping/list"><i
								class="fa fa-circle-o"></i>Shipping Charge List</a></li>
					</sec:authorize>

					<li><a
						href="${contextPath }/accounts/master/generalSupplier/list"><i
							class="fa fa-circle-o"></i>General Supplier</a></li>
					<li><a href="${contextPath }/master/hsCode/list"><i
							class="fa fa-circle-o"></i>HS Code</a></li>
					<li><a
						href="${contextPath }/accounts/master/shippingTerms/list"><i
							class="fa fa-circle-o"></i>Shipping Terms</a></li>

					<li><a
						href="${contextPath }/accounts/master/shippingMarks/list"><i
							class="fa fa-circle-o"></i>Shipping Marks</a></li>
					<li><a
						href="${contextPath }/accounts/master/specialExchageRate/list"><i
							class="fa fa-circle-o"></i>Special Exchage Rates</a></li>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(28)">
			<li><a href="${contextPath }/accounts/user/management"><i
					class="fa fa-user-plus"></i><span>User Management</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(29)">
			<li class="treeview"><a href="#"> <i class="fa  fa-jpy"></i>
					<span>Loan Management</span> <span class="pull-right-container">
						<i class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(30)">
						<li><a href="${contextPath }/accounts/create-loan"><i
								class="fa fa-circle-o"></i>Create Loan</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(31)">
						<li><a href="${contextPath }/accounts/loan-details"><i
								class="fa fa-circle-o"></i>View Loan</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(32)">
						<li><a href="${contextPath }/accounts/re-payment"><i
								class="fa fa-circle-o"></i>Re payment</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(33)">
			<li class="treeview"><a href="#"> <i class="fa fa-clone"></i>
					<span>BL Management</span> <span class="pull-right-container">
						<i class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(34)">
						<li><a href="${contextPath }/accountsBL/page"><i
								class="fa fa-circle-o"></i>BL Management</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(35)">
						<li><a href="${contextPath }/accountsBL/cr-management/page"><i
								class="fa fa-circle-o"></i>CR Management</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(36)">
						<li><a
							href="${contextPath }/accounts/billoflanding/form-view"><i
								class="fa fa-circle-o"></i>Sri Lankan Management</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(37)">
			<li><a href="${contextPath }/accounts/executegl/gl"><i
					class="fa fa-bank"></i><span>Execute GL</span></a></li>
		</sec:authorize>
		<sec:authorize access="canAccess(38)">
			<li class="treeview"><a href="#"> <i
					class="fa fa-line-chart"></i> <span>Branch Sales Order</span> <span
					class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(39)">
						<li><a
							href="${contextPath }/accounts/create/branch-salesOrder"><i
								class="fa fa-circle-o"></i>Create Order</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(40)">
						<li><a href="${contextPath }/accounts/branch-salesOrder/list"><i
								class="fa fa-circle-o"></i>List</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<!-- Customer Accounts -->
		<!-- report menu -->
		<sec:authorize access="canAccess(41)">
			<li class="treeview"><a href="#"> <i
					class="fa fa-institution"></i> <span>Inventory Reports</span> <span
					class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(42)">
						<li><a href="${contextPath }/accounts/report/stockSales"><i
								class="fa fa-circle-o"></i>Stock Sales</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(43)">
						<li><a href="${contextPath }/inventory/inventory"><i
								class="fa fa-circle-o"></i>Inventory</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(44)">
						<li><a href="${contextPath }/accounts/report/sales-summary"><i
								class="fa fa-circle-o"></i>Sales Summary</a></li>
					</sec:authorize>
					<li><a href="${contextPath }/accounts/salesOrder"><i
							class="fa fa-circle-o"></i>Sales Order Edit / Cancel</a></li>
					<sec:authorize access="canAccess(45)">
						<li><a
							href="${contextPath }/accounts/report/inventoryValueReport"><i
								class="fa fa-circle-o"></i>Inventory Value</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(46)">
						<li><a
							href="${contextPath }/accounts/report/income-by-customer-report"><i
								class="fa fa-circle-o"></i>Income by Customer</a></li>
					</sec:authorize>
					<li><a href="${contextPath }/accounts/report/profit/loss"><i
							class="fa fa-circle-o"></i>Profit Loss</a></li>
				</ul></li>
		</sec:authorize>

		<sec:authorize access="canAccess(47)">
			<li class="treeview"><a href="#"> <i class="fa fa-money"></i>
					<span>Finance Reports</span> <span class="pull-right-container">
						<i class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(48)">
						<li><a
							href="${contextPath }/accounts/report/accountTransaction"><i
								class="fa fa-circle-o"></i>Account Transaction</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(49)">
						<li><a href="${contextPath }/accounts/report/trailBalance"><i
								class="fa fa-circle-o"></i>Trial Balance</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(50)">

						<li><a href="${contextPath }/accounts/report/gl-report"><i
								class="fa fa-circle-o"></i>GL Report</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(51)">
						<li><a
							href="${contextPath }/accounts/report/trailBalanceTransaction"><i
								class="fa fa-circle-o"></i>GL Transaction</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(52)">
						<li><a href="${contextPath }/accounts/report/bank-statement"><i
								class="fa  fa-circle-o"></i>Bank Statement</a></li>

					</sec:authorize>
					<sec:authorize access="canAccess(53)">
						<li><a
							href="${contextPath }/accounts/report/balanceStatement"><i
								class="fa  fa-circle-o"></i>Balance Sheet Statement</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(54)">
						<li><a href="${contextPath }/accounts/report/profitAndLoss"><i
								class="fa  fa-circle-o"></i>Profit &amp; Loss</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="canAccess(55)">
			<li class="treeview"><a href="#"> <i class="fa fa-money"></i>
					<span>Payable &amp; Receivable Reports</span> <span
					class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<sec:authorize access="canAccess(56)">
						<li><a href="${contextPath }/accounts/payment/payable-amount"><i
								class="fa fa-circle-o"></i>Accounts Payable</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(57)">
						<li><a
							href="${contextPath }/accounts/payment/receivable-amount"><i
								class="fa fa-circle-o"></i>Accounts Receivable</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(58)">
						<li><a
							href="${contextPath }/accounts/report/supplierStatement"><i
								class="fa  fa-circle-o"></i>Supplier Statement</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(59)">
						<li><a href="${contextPath }/accounts/customer-accounts"><i
								class="fa fa-circle-o"></i>Customer Accounts</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(60)">
						<li><a href="${contextPath }/accounts/claim/tax"><i
								class="fa fa-circle-o"></i>Claim</a></li>
					</sec:authorize>
					<sec:authorize access="canAccess(61)">
						<li><a
							href="${contextPath }/accounts/report/ar-aging-summary"><i
								class="fa fa-circle-o"></i>A/R Aging Summary</a></li>
					</sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="hasRole('ADMIN')">
			<li><a href="${contextPath }/accounts/uac"><i
					class="fa fa-users"></i><span>User Access Control</span></a></li>
		</sec:authorize>

		<li><a href="${contextPath }/accounts/stockInfo"><i
				class="fa fa-info"></i><span>Stock Info</span></a></li>


		<li class="save-side-menu-state" data-menu-parent="stock-tracker"
			data-menu-child="" data-role="parent"><a
			href="${contextPath }/accounts/custom/report"><i
				class="fa fa-file-text-o"></i> <span>Stock Organizer</span></a></li>

	</section>
	<!-- /.sidebar -->
</aside>
