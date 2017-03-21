package org.sugarframework.test;

import java.util.ArrayList;
import java.util.List;

import org.sugarframework.Column;
import org.sugarframework.Container;
import org.sugarframework.Order;
import org.sugarframework.Row;
import org.sugarframework.View;
import org.sugarframework.WithinColumn;
import org.sugarframework.component.ColumnWidth;
import org.sugarframework.component.common.Descriptor;
import org.sugarframework.component.common.UnorderedList;

@View(value="Layout", url="layout", icon="glyphicon glyphicon-asterisk")
@Container(rows = {
	@Row(value="row2", columns = {
		@Column(value="beer", width=ColumnWidth.TWELVE),
		@Column(value="container1", width=ColumnWidth.EIGHT),
		@Column(value="container5", width=ColumnWidth.FOUR),
		@Column(value="container3", width=ColumnWidth.FOUR)
	}),
	@Row(value="row5", columns = {
		@Column(value="container4", width=ColumnWidth.FOUR),
		@Column(value="container7", width=ColumnWidth.SIX),
		@Column(value="container6", width=ColumnWidth.SIX)
	}),
	@Row(value="row4"),
	@Row(value="row1", columns = {
		@Column(value="container10", width=ColumnWidth.TWO),
		@Column(value="container12", width=ColumnWidth.TWO),
		@Column(value="container8", width=ColumnWidth.TWO),
		@Column(value="container9", width=ColumnWidth.TWO),
		@Column(value="container11", width=ColumnWidth.TWO),
		@Column(value="container13", width=ColumnWidth.TWO)
	}),
	@Row(value="row3", columns = {
		@Column(value="container2", width=ColumnWidth.FOUR)
	})

})
public class LayoutTest {
		
	@Order(1) @UnorderedList @WithinColumn("container1") private List<String> items = new ArrayList<String>();
	{
		items.add("Cras justo odio");
		items.add("Dapibus ac facilisis in");
		items.add("Morbi leo risus");
		items.add("Porta ac consectetur ac");
		items.add("Vestibulum at eros");
	}
	
	@Order(2) @Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container1")
	Void d1;
	
	@Order(2) @Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container2")
	Void d2;
	
	@Order(1) @Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container2")
	Void d2a;
	
	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container3")
	Void d3;
	
	@Order(1) @Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container4")
	Void d4;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" })
	@WithinColumn("container5")
	Void d5;
	
	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container6")
	Void d6;
	
	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container7")
	Void d7;
	
	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container8")
	Void d8;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container9")
	Void d9;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container10")
	Void d10;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container11")
	Void d11;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container12")
	Void d12;

	@Descriptor({ "Container1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("container13")
	Void d13;
	
	@Order(1) @Descriptor({ "Beer3", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("beer")
	Void b3;
	
	@Order(2) @Descriptor({ "Beer1", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("beer")
	Void b;
	
	@Order(3) @Descriptor({ "Beer2", "Navbars are responsive meta components that serve as navigation headers for your application or site" }) 
	@WithinColumn("beer")
	Void b2;


}
