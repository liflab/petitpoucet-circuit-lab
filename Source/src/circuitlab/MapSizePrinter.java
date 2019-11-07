package circuitlab;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.PrintHandler;
import ca.uqac.lif.azrael.ReflectionPrintHandler;
import ca.uqac.lif.azrael.size.SizePrinter;

public class MapSizePrinter extends ObjectPrinter<MapSizePrinter.SizeEntry>
{
	public MapSizePrinter()
	{
		super();
		m_handlers.add(new NullPrintHandler(this));
		m_handlers.add(new NumberPrintHandler());
		m_handlers.add(new StringPrintHandler(this));
		m_handlers.add(new BooleanPrintHandler());
		m_handlers.add(new CollectionPrintHandler(this));
		m_handlers.add(new ArrayPrintHandler(this));
		m_reflectionHandler = new SizeReflectionHandler(this);
		m_usePrintable = false;
	}
	
	public static abstract class ReferencePrintHandler implements PrintHandler<MapSizePrinter.SizeEntry>
	{
		protected IdentityHashMap<Object,Integer> m_seenObjects;
		
		protected MapSizePrinter m_printer;
		
		public ReferencePrintHandler(MapSizePrinter printer)
		{
			super();
			m_printer = printer;
			m_seenObjects = new IdentityHashMap<Object,Integer>();
		}
		
		@Override
		public final SizeEntry handle(Object o) throws PrintException 
		{
			// We count objects only once
			if (m_seenObjects.containsKey(o))
			{
				return new SizeEntry(o, 0);
			}
			m_seenObjects.put(o, 1);
			return getSize(o);
		}
		
		public abstract SizeEntry getSize(Object o) throws PrintException;
		
		@Override
		public void reset()
		{
			m_seenObjects.clear();
		}
	}
	
	public class BooleanPrintHandler implements PrintHandler<SizeEntry>
	{

		@Override
		public boolean canHandle(Object o) 
		{
			return o instanceof Boolean;
		}

		@Override
		public SizeEntry handle(Object o) throws PrintException 
		{
			return new SizeEntry(o, SizePrinter.BOOLEAN_FIELD_SIZE);
		}
		
		@Override
		public void reset()
		{
			// Nothing to do
		}

	}
	
	protected static class ArrayPrintHandler extends ReferencePrintHandler
	{
		public ArrayPrintHandler(MapSizePrinter printer)
		{
			super(printer);
		}
		
		@Override
		public boolean canHandle(Object o) 
		{
			return o != null && o.getClass().isArray();
		}

		@Override
		public SizeEntry getSize(Object o) throws PrintException 
		{
			SizeEntry se = new SizeEntry(o, 16);
			int length = Array.getLength(o);
			for (int i = 0; i < length; i++)
			{
				Object elem = Array.get(o, i);
				se.addChild(m_printer.print(elem));
				if (!SizePrinter.isPrimitive(elem))
				{
					// If the array does not store primitive values,
					// the size of an entry is that of a pointer
					se.addToSize(SizePrinter.OBJREF_SIZE);
				}
			}
			return se;
		}
	}
	
	public static class SizeEntry
	{
		protected int m_size;
		
		protected Object m_object;
		
		protected List<SizeEntry> m_children;
		
		public SizeEntry(Object o, int size)
		{
			super();
			m_children = new ArrayList<SizeEntry>();
			m_object = o;
			m_size = size;
		}
		
		public void addToSize(int s)
		{
			m_size += s;
		}
		
		public void addChild(SizeEntry se)
		{
			m_children.add(se);
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			toString(sb, "");
			return sb.toString();
		}
		
		protected void toString(StringBuilder sb, String indent)
		{
			sb.append(indent);
			if (m_object != null)
			{
				sb.append(m_object.getClass().getSimpleName()).append("\t").append(m_size).append("\n");
			}
			String new_indent = indent + " ";
			for (SizeEntry se : m_children)
			{
				se.toString(sb, new_indent);
			}
		}
	}
	
	public class CollectionPrintHandler extends ReferencePrintHandler
	{
		public CollectionPrintHandler(MapSizePrinter printer)
		{
			super(printer);
		}
		
		@Override
		public boolean canHandle(Object o) 
		{
			return o instanceof Collection || o instanceof Map;
		}

		@Override
		public SizeEntry getSize(Object o) throws PrintException 
		{
			if (o instanceof HashSet)
			{
				HashSet<?> col = (HashSet<?>) o;
				int size = 16 + 64 + 36 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Object elem : col)
				{
					se.addChild(m_printer.print(elem));
				}
				return se;
			}
			if (o instanceof HashMap)
			{
				HashMap<?,?> col = (HashMap<?,?>) o;
				int size = 64 + 36 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Map.Entry<?,?> entry : col.entrySet())
				{
					se.addChild(m_printer.print(entry.getKey()));
					se.addChild(m_printer.print(entry.getValue()));
				}
				return se;
			}
			if (o instanceof Hashtable)
			{
				Hashtable<?,?> col = (Hashtable<?,?>) o;
				int size = 56 + 36 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Map.Entry<?,?> entry : col.entrySet())
				{
					se.addChild(m_printer.print(entry.getKey()));
					se.addChild(m_printer.print(entry.getValue()));
				}
				return se;
			}
			if (o instanceof LinkedList)
			{
				LinkedList<?> col = (LinkedList<?>) o;
				int size = 24 + 24 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Object elem : col)
				{
					se.addChild(m_printer.print(elem));
				}
				return se;
			}
			if (o instanceof ArrayList)
			{
				ArrayList<?> col = (ArrayList<?>) o;
				int size = 48 + 4 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Object elem : col)
				{
					se.addChild(m_printer.print(elem));
				}
				return se;
			}
			if (o instanceof ArrayDeque)
			{
				ArrayDeque<?> col = (ArrayDeque<?>) o;
				int size = 48 + 4 * col.size();
				SizeEntry se = new SizeEntry(o, size);
				for (Object elem : col)
				{
					se.addChild(m_printer.print(elem));
				}
				return se;
			}
			return new SizeEntry(o, 0);
		}
	}
	
	public static class NumberPrintHandler implements PrintHandler<SizeEntry>
	{

		@Override
		public boolean canHandle(Object o) 
		{
			return o instanceof Number;
		}

		@Override
		public SizeEntry handle(Object o) throws PrintException 
		{
			if (o instanceof Integer)
			{
				return new SizeEntry(o, SizePrinter.INT_FIELD_SIZE);
			}
			if (o instanceof Short)
			{
				return new SizeEntry(o, SizePrinter.SHORT_FIELD_SIZE);
			}
			if (o instanceof Byte)
			{
				return new SizeEntry(o, SizePrinter.BYTE_FIELD_SIZE);
			}
			if (o instanceof Long)
			{
				return new SizeEntry(o, SizePrinter.LONG_FIELD_SIZE);
			}
			if (o instanceof Float)
			{
				return new SizeEntry(o, SizePrinter.FLOAT_FIELD_SIZE);
			}
			if (o instanceof Double)
			{
				return new SizeEntry(o, SizePrinter.DOUBLE_FIELD_SIZE);
			}
			return new SizeEntry(o, 0);
		}

		@Override
		public void reset()
		{
			// Nothing to do
		}

	}
	
	public static class SizeReflectionHandler extends ReflectionPrintHandler<SizeEntry>
	{
		protected IdentityHashMap<Object,Integer> m_seenObjects;

		public SizeReflectionHandler(MapSizePrinter s) 
		{
			super(s);
			m_ignoreTransient = false;
			m_seenObjects = new IdentityHashMap<Object,Integer>();
		}

		@Override
		public SizeEntry handle(Object o) throws PrintException
		{
			// We count objects only once
			if (m_seenObjects.containsKey(o))
			{
				return new SizeEntry(o, 0);
			}
			m_seenObjects.put(o, 1);
			int size = SizePrinter.OBJECT_SHELL_SIZE; // Basic overhead of a Java object
			SizeEntry se = new SizeEntry(o, size);
			for (Field field : getAllFields(o.getClass()))
			{
				// Is this field declared as transient?
				if (m_ignoreTransient && Modifier.isTransient(field.getModifiers()))
				{
					// Yes: don't serialize this field
					continue; 
				}
				field.setAccessible(true);
				
				try
				{
					Object f_v = field.get(o);
					if (SizePrinter.isPrimitive(f_v))
					{
						SizeEntry sub_se = new NamedSizeEntry(field.getName(), f_v, 0);
						sub_se.addChild(m_printer.print(f_v));
						se.addChild(sub_se);
					}
					else
					{
						SizeEntry sub_se = new NamedSizeEntry(field.getName(), f_v, 0);
						sub_se.addChild(m_printer.print(f_v));
						se.addChild(sub_se);
						se.addToSize(SizePrinter.OBJREF_SIZE);
					}
				} 
				catch (IllegalArgumentException e)
				{
					throw new PrintException(e);
				} 
				catch (IllegalAccessException e)
				{
					throw new PrintException(e);
				}
			}
			return se;
		}
		
		@Override
		public SizeEntry encapsulateFields(Object o, Map<String,Object> contents) throws PrintException
		{
			return new SizeEntry(o, 0);
		}
		
		@Override
		public void reset()
		{
			m_seenObjects.clear();
		}
	}
	
	public static class NullPrintHandler extends ReferencePrintHandler
	{
		public NullPrintHandler(MapSizePrinter printer)
		{
			super(printer);
		}

		@Override
		public boolean canHandle(Object o) 
		{
			return o == null;
		}

		@Override
		public SizeEntry getSize(Object o) throws PrintException 
		{
			return new SizeEntry(o, 0);
		}
	}
	
	public static class NamedSizeEntry extends SizeEntry
	{
		protected String m_name;
		
		public NamedSizeEntry(String name, Object o, int size)
		{
			super(o, size);
			m_name = name;
		}
		
		@Override
		protected void toString(StringBuilder sb, String indent)
		{
			sb.append(indent);
			sb.append(m_name).append("\t").append(m_size).append("\n");
			String new_indent = indent + " ";
			for (SizeEntry se : m_children)
			{
				se.toString(sb, new_indent);
			}
		}
	}
	
	public static class StringPrintHandler extends ReferencePrintHandler
	{
		public StringPrintHandler(MapSizePrinter printer)
		{
			super(printer);
		}

		@Override
		public boolean canHandle(Object o) 
		{
			return o instanceof String;
		}

		@Override
		public SizeEntry getSize(Object o) throws PrintException 
		{
			String s = (String) o;
			return new SizeEntry(o, 28 + SizePrinter.CHAR_FIELD_SIZE * s.length());
		}

	}

	@Override
	public SizeEntry wrap(Object o, SizeEntry t) throws PrintException
	{
		return t;
	}
	
	/**
	 * Checks if an object is a primitive
	 * @param o The object
	 * @return <tt>true</tt> if the object is primitive, <tt>false</tt>
	 * otherwise
	 */
	public static boolean isPrimitive(Object o)
	{
		return o == null || o instanceof Boolean || o instanceof Number || o instanceof String;
	}
}
