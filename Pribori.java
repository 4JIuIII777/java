
import java.util.*;
import java.lang.*;

import org.junit.Test;




class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(ApartmentAppTest.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
}  

class ApartmentAppTest
{
	@Test
	public void shouldFulfillUseCaseSuccessfully() {
		// cоздать квартиру с устройствами
		DeviceFactory deviceFactory = new DeviceFactory();
		List<Device> devices = deviceFactory.generateDevicesInQuantity(10);
		ApartmentFactory apartmentFactory = new ApartmentFactory();
		Apartment apartment = apartmentFactory.getApartment(devices);
 
		// подключить некоторые устройства
		for (Device device : apartment.getAllDevices()) {
			apartment.plugIn(device);
		}
 
		// рассчитать потребляемую мощность и дисплей
		DisplayHelper helper = new DisplayHelper();
		helper.displayInConsole("" + apartment.getTotalPower());
 
		// сортировка устройств по мощности и отображению до и после сортировки
		AbstractDeviceSorter sorter = new DeviceSorter(apartment);
		helper.displayInConsole("" + apartment.getAllDevices());
		sorter.sortDevicesByPower();
		helper.displayInConsole("" + apartment.getAllDevices());
 
		//нахождение устройства, соответствующее определенному диапазону
		
		PowerRange powerRange = new PowerRange(300, 800);
		ModelRange modelRange = new ModelRange("aa", "gg");
		SearchEngine engine = new SearchEngine(apartment);
		Device device = engine.findDeviceByModelAndPower(modelRange, powerRange);
		helper.displayInConsole("" + device);
	}
}


interface DeviceContainer {
	

	int getNumberOfDevices();
	Device getDeviceById(Integer id);
	List<Device> getAllDevices();
	void addDevice(Device device);
	void addAllDevices(List<Device> devices);
	void clear();
	void removeDevice(Device device);
	void removeDeviceById(Integer id);
	void removeAllDevices(List<Device> devices);
	

}

interface Plugging {
	
	void plugIn(Device device);
	void plugInAll(List<Device> devices);
	void unplug(Device device);
	void unplugAll(List<Device> devices);
}

class Apartment implements DeviceContainer, Plugging {
	
	private List<Device> devices;
	
	public Apartment(List<Device> devices) {
		this.devices = devices;
	}

	public int getNumberOfDevices() {
		return devices.size();
	}
	
	public Device getDeviceById(Integer id){
		return devices.get(id);
	}
	
	public List<Device> getAllDevices() {
		return this.devices; 
	}
	
	public void addDevice(Device device){
		devices.add(device);
	}
	
	public void addAllDevices(List<Device> devices){
		devices.addAll(devices);
	}
	
	public void clear(){
		devices.clear();
	}
	
	public void removeDevice(Device device){
		devices.remove(device);
	}
	
	public void removeDeviceById(Integer id){
		devices.remove(id);
	}
	
	public void removeAllDevices(List<Device> devices){
		devices.removeAll(devices);
	}
	

	public void plugIn(Device device){
		device.plugIn();
	}
	
	public void plugInAll(List<Device> devices){
		for (Device device : devices) {
			plugIn(device);
		}
	}
	
	public void unplug(Device device){
		device.unplug();
	}
	
	public void unplugAll(List<Device> devices){
		for (Device device : devices) {
			unplug(device);
		}
	}
	
	public int getTotalPower() {
		int counter = 0;
		for (Device device : devices) {
			counter += device.getPower();
		}
		return counter;
	}
}

interface Pluggable {
	
	void plugIn();
	void unplug();
	boolean isPlugged(); 
}

abstract class Device implements Pluggable {
	
	private Integer id;
	private String model;
	private Integer power;
	private boolean isPlugged;
	
	Integer getId() {
		return this.id;
	}
 
	void setId(Integer id) {
		this.id = id;
	}
 
	String getModel() {
		return this.model;
	}
 
	void setModel(String model) {
		this.model = model;
	}
 
	Integer getPower() {
		return this.power;
	}
 
	void setPower(Integer power) {
		this.power = power;
	}
	
	public void plugIn() {
		this.isPlugged = true;
	}
	
	public void unplug() {
		this.isPlugged = false;
	}
	
	public boolean isPlugged() {
		return this.isPlugged;
	}
}

class Computer extends Device {
	
	public void plugIn() {
		super.plugIn();
		
	}
	
	public void unplug() {
		super.unplug();
	
	}
}

class SearchEngine {
 
	private Apartment apartment;
 
	public SearchEngine(Apartment apartment) {
		this.apartment = apartment;
	}
 
	public Device findDeviceByModelAndPower(Range modelRange, Range powerRange) {
	
		return apartment.getDeviceById(1);
	}
}
 
abstract class Range<T extends Comparable<? super T>> {
 
	private T lowerLimit;
	private T upperLimit;
 
	public Range(T lowerLimit, T upperLimit) {
		setLimits(lowerLimit, upperLimit);
	}
 
	private void setLimits(T lowerLimit, T upperLimit) {
		if (lowerLimit.compareTo(upperLimit) <= 0) {
			this.upperLimit = upperLimit;
			this.lowerLimit = lowerLimit;
		} else {
			this.upperLimit = lowerLimit;
			this.lowerLimit = upperLimit;
		}
	}
 
	public boolean includes(T arg) {
		return arg.compareTo(lowerLimit) >= 0 && arg.compareTo(upperLimit) <= 0;
	}
}
 
class ModelRange extends Range<String> {
	public ModelRange(String lowerLimit, String upperLimit) {
		super(lowerLimit, upperLimit);
	}
}
 
class PowerRange extends Range<Integer> {
	public PowerRange(Integer lowerLimit, Integer upperLimit) {
		super(lowerLimit, upperLimit);
	}
}
 
interface AbstractDeviceSorter {
	void sortDevicesByPower();
}
 
class DeviceSorter implements AbstractDeviceSorter{
 
	private Apartment apartment;
 
	public DeviceSorter(Apartment apartment) {
		this.apartment = apartment;
	}
 
	public void sortDevicesByPower() {
	
	}
}
 
class DisplayHelper {
 
	private Apartment apartment;
 
	public DisplayHelper() {
	 
	}
 
	public void displayInConsole(String string) {
		System.out.print(string);
	}
}
 
class ApartmentFactory {
 
	public Apartment getApartment(List<Device> devices) {
		return new Apartment(devices);
	}
}
 
class DeviceFactory {
 
	private static Integer idCounter = 0;
	private static Map<Integer, String> devices = new HashMap<>();
 
	static {
		devices.put(1, "Computer");
		devices.put(2, "TVSet");
	}
 
	List<Device> generateDevicesInQuantity(int quantity) {
		List<Device> devices = new ArrayList<>(quantity);
		for (int i = 0; i < quantity; i++) {
			devices.add(generateDevice());
		}
		return devices;
	}
 
	private Device generateDevice() {
 
		Device device = (Device)generateRandomSubClass();
 
		setProperties(device);
 
		return device;
	}
 
	private Object generateRandomSubClass() { 
 
		Random random = new Random();
		int i = random.nextInt(devices.size() - 1);
 
		Constructor<?> cons = null;
		Object object = null;
 
		try {
			Class c = Class.forName(devices.get(1)); 
		
            object = c.newInstance();
		} catch (Exception e) {
            e.printStackTrace();
		}
 
		return object;
	}
 
	private void setProperties(Device device) {
		device.setId(++idCounter);
		device.setModel(generateModel());
		device.setPower(generateCapacity());
	}
 
	private String generateModel() {
		
		return "fhfhf";
	}
 
	private Integer generateCapacity() {
		Random random = new Random();
		return 100 + random.nextInt(900);
	}
}




