package org.sugarframework.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.sugarframework.DefaultValue;
import org.sugarframework.Label;
import org.sugarframework.Order;
import org.sugarframework.Validate;
import org.sugarframework.View;
import org.sugarframework.component.BackgroundStyle;
import org.sugarframework.component.ListStyle;
import org.sugarframework.component.ParagraphStyle;
import org.sugarframework.component.Style;
import org.sugarframework.component.TextStyle;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Address;
import org.sugarframework.component.common.BlockQuote;
import org.sugarframework.component.common.Breadcrumb;
import org.sugarframework.component.common.ButtonGroup;
import org.sugarframework.component.common.DescriptionList;
import org.sugarframework.component.common.Descriptor;
import org.sugarframework.component.common.Html;
import org.sugarframework.component.common.Image;
import org.sugarframework.component.common.Paragraph;
import org.sugarframework.component.common.PrettyDescriptor;
import org.sugarframework.component.common.SugarLabel;
import org.sugarframework.component.common.Table;
import org.sugarframework.component.common.UnorderedList;
import org.sugarframework.security.RestrictRole;
import org.sugarframework.test.component.HelloWorld;
import org.sugarframework.test.vo.Animal;

@View(value = "Components", url = "component", icon = "glyphicon glyphicon-th")
@Order(4)
public class ComponentTest {

	@Order(2)
	@HelloWorld(heading = "#{generic.title}")
	private String value = "Yo mang, what's up?";

	@Order(11)
	@Breadcrumb({ "Home", "Water", "Swimming" })
	Void b;

	@Order(6)
	@DescriptionList("#{components.desc.list.one}")
	Void d;

	@Order(4)
	@Image("sweetsugar.png")
	Void image;
	
	@Order(100)
	@Image("${me.randomImage()}")
	Void image2;

	@Order(15)
	@Html("#{components.desc.h1.heading}")
	Void h1;

	@Order(14)
	@Html("#{components.desc.h2.heading}")
	Void h2;

	@Order(13)
	@Html("#{components.desc.h3.heading}")
	Void h3;

	@Order(12)
	@Html("#{components.desc.h4.heading}")
	Void h4;

	@Order(9)
	@Html("#{components.desc.h5.heading}")
	Void h5;

	@Order(8)
	@Html("#{components.desc.h6.heading}")
	Void h6;

	@Order(16)
	@Paragraph("#{components.paragraph}")
	Void p;

	@Order(23)
	@Paragraph(value = "#{components.paragraph}", style = ParagraphStyle.PARAGRAPH_LEAD, textStyle = TextStyle.DANGER)
	Void p2;

	@Order(26)
	@Paragraph(value = "#{components.paragraph}", textStyle = TextStyle.MUTED)
	Void p3;

	@Order(25)
	@Paragraph(value = "#{components.paragraph}", textStyle = TextStyle.INFO)
	Void p4;

	@Order(24)
	@Paragraph(value = "#{components.paragraph}", textStyle = TextStyle.WARNING)
	Void p5;

	@Order(27)
	@Paragraph(value = "#{components.paragraph}", backgroundStyle = BackgroundStyle.INFO)
	Void p6;

	@Order(0)
	@Address({ "Sugar Framework Inc.", "12345 Internet Way, Suite 45",
			"San Francisco, CA 94107", "P: (123) 456-7890" })
	Void address;

	@Order(17)
	@BlockQuote({ "#{components.blockquote.message}",
			"#{components.blockquote.source}" })
	Void blockQuote;

	@Order(21)
	@BlockQuote(value = { "#{components.blockquote.message}" }, reverse = true)
	Void blockQuote2;

	@Order(18)
	@Descriptor({ "#{components.descriptor.title}", "#{components.descriptor}" })
	Void d1;

	@Order(20)
	@PrettyDescriptor(value = { "#{components.descriptor.title}",
			"#{components.descriptor}" }, style = Style.STYLE_WARNING)
	Void d2;

	@Order(22)
	@UnorderedList(style = ListStyle.INLINE)
	private List<String> items = new ArrayList<String>();
	{
		items.add("Cras justo odio");
		items.add("Dapibus ac facilisis in");
		items.add("Morbi leo risus");
		items.add("Porta ac consectetur ac");
		items.add("Vestibulum at eros");
	}

	@Order(7)
	@UnorderedList
	private List<String> items2 = new ArrayList<String>();
	{
		items2.add("Cras justo odio");
		items2.add("Dapibus ac facilisis in");
		items2.add("Morbi leo risus");
		items2.add("Porta ac consectetur ac");
		items2.add("Vestibulum at eros");
	}

	@Order(5)
	@UnorderedList(style = ListStyle.UNSTYLED)
	private List<String> items3 = new ArrayList<String>();
	{
		items3.add("Cras justo odio");
		items3.add("Dapibus ac facilisis in");
		items3.add("Morbi leo risus");
		items3.add("Porta ac consectetur ac");
		items3.add("Vestibulum at eros");
	}

	@Order(3)
	@Table
	@Label("#{components.zoo.label}")
	private Collection<Animal> animals = new ArrayList<Animal>();
	{
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
		animals.add(new Animal(2123, "Cha cha", "Canine", new Date(), 67l,
				false));
	}

	@Order(19)
	@SugarLabel(value = "#{components.label}", style = Style.STYLE_DANGER)
	Void l;

	@Order(1)
	@ButtonGroup
	private Map<String, String> buttonGroup = new LinkedHashMap<String, String>();
	{
		buttonGroup.put("one", "navigation one");
		buttonGroup.put("two", "navigation two");
		buttonGroup.put("three", "navigation three");
	}

	@Order(10)
	@Action("Lets test")
	public void testBean(
			@Label("Zoo Name") String zooName,
			@Label("Zoo Location") String zooLocation,
			@Label("Int Test") int intvalue,
			@Label("#{beantest.default.param.title}") @DefaultValue("#{beantest.default.param.value}") String anotherString,
			@Label("Opening Date") Date opening,
			@Label("Postal Code") @Validate("validatePostalCodeFormat") String postalCode,
			@Label("Animal Info") Animal animal) {
		System.out.println(animal);
	}

	public String randomImage() {
		return "sweetsugar.png";
	}

}
