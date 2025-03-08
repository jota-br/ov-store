package ostro.veda.test;

import ostro.veda.common.dto.*;
import ostro.veda.common.validation.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper {

    private int getRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    private String getRandomName() {
        List<String> names = List.of(
                "Liam", "Olivia", "Noah", "Emma", "James", "Ava", "Benjamin",
                "Sophia", "Lucas", "Isabella", "Henry", "Amelia", "Alexander",
                "Mia", "Mason", "Charlotte", "Elijah", "Evelyn", "William",
                "Harper", "Ethan", "Scarlett", "Logan", "Abigail", "Jacob",
                "Luna", "Michael", "Ella", "Daniel", "Chloe"
        );

        return names.get(getRandomInt(names.size()));
    }

    private String getRandomLastName() {
        List<String> lastNames = List.of(
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia",
                "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez",
                "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
                "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez",
                "Thompson", "White", "Harris", "Sanchez", "Clark",
                "Ramirez", "Lewis", "Robinson"
        );

        return lastNames.get(getRandomInt(lastNames.size()));
    }

    private String getRandomUsername() {
        List<String> usernames = List.of(
                "PixelPro_01", "ShadowFury-99", "LunaLight@23", "CyberNinja_77", "EchoWave-88",
                "BlazeHunter@100", "AquaWhiz_42", "IronClad-09", "StellarNova@55", "FrostByte_33",
                "CrimsonWolf-07", "QuantumShift@81", "GalaxyRider_66", "TurboTiger-21", "MysticAce@17",
                "RocketCore_30", "PhantomStrike@49", "SkyDancer-73", "LightningFox_15", "CosmicVibe@90",
                "NeonPulse-11", "BlizzardTrail@28", "SolarPhantom_58", "ShadowEcho@39", "WildFalcon-03",
                "NovaDash_25", "ArcticFlare@77", "ThunderHawk_02", "VenomEdge-19", "StealthBlade@01"
        );

        return usernames.get(getRandomInt(usernames.size()));
    }

    private String getRandomEmail() {
        List<String> emails = List.of(
                "liam01@example.com", "olivia99@example.com", "noah23@example.net",
                "emma77@example.org", "james88@example.com", "ava100@example.net",
                "benjamin42@example.com", "sophia09@example.org", "lucas55@example.net",
                "isabella33@example.com", "henry07@example.net", "amelia81@example.org",
                "alexander66@example.net", "mia21@example.com", "mason17@example.org",
                "charlotte30@example.net", "elijah49@example.org", "william73@example.net",
                "ethan15@example.com", "scarlett90@example.org", "logan11@example.net",
                "abigail28@example.org", "jacob58@example.com", "luna39@example.net",
                "michael03@example.org", "ella25@example.net", "daniel77@example.com",
                "chloe02@example.org", "natalie19@example.net", "jackson01@example.com"
        );

        return emails.get(getRandomInt(emails.size()));
    }

    private String getRandomPhone() {
        List<String> phoneNumbers = List.of(
                "+12025550123", "+442079460123", "+33123456789",
                "+49301234567", "+918067890123", "+81312345678",
                "+617123456", "+5511987654321", "+349112345678",
                "+3961234567", "+861087654321", "+74951234567",
                "+27213456789", "+312023456789", "+644567890",
                "+82234567890", "+6512345678", "+4681234567",
                "+60398765432", "+352123456", "+5371234567",
                "+13459491234", "+23412345678", "+92213456789",
                "+351211234567", "+96512345678", "+97444123456",
                "+43112345678", "+375171234567", "+358987654321"
        );

        return phoneNumbers.get(getRandomInt(phoneNumbers.size()));
    }

    private String getRandomStreetAddress() {
        List<String> streetAddresses = List.of(
                "Maple Street", "Oak Avenue", "Pine Lane",
                "Birch Road", "Cedar Court", "Elm Drive",
                "Aspen Boulevard", "Walnut Street", "Cherry Place",
                "Willow Way", "Spruce Terrace", "Poplar Parkway",
                "Redwood Circle", "Sycamore Alley", "Dogwood Lane",
                "Magnolia Avenue", "Cypress Trail", "Hickory Street",
                "Cottonwood Boulevard", "Alder Drive", "Juniper Court",
                "Fir Road", "Larch Lane", "Beech Way",
                "Chestnut Circle", "Ash Avenue", "Linden Place",
                "Pear Parkway", "Olive Terrace", "Sequoia Street"
        );

        return streetAddresses.get(getRandomInt(streetAddresses.size()));
    }

    private String getRandomCity() {
        List<String> cities = List.of(
                "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
                "Philadelphia", "San Antonio", "San Diego", "Dallas", "Austin",
                "San Jose", "Fort Worth", "Jacksonville", "Columbus", "Charlotte",
                "Indianapolis", "San Francisco", "Seattle", "Denver", "Boston",
                "El Paso", "Nashville", "Detroit", "Portland", "Memphis",
                "Oklahoma City", "Las Vegas", "Louisville", "Baltimore", "Milwaukee"
        );

        return cities.get(getRandomInt(cities.size()));
    }

    private String getRandomState() {
        List<String> states = List.of(
                "California", "Texas", "Florida", "New York", "Pennsylvania",
                "Illinois", "Ohio", "Georgia", "North Carolina", "Michigan",
                "New Jersey", "Virginia", "Washington", "Arizona", "Massachusetts",
                "Tennessee", "Indiana", "Missouri", "Maryland", "Wisconsin",
                "Colorado", "Minnesota", "South Carolina", "Alabama", "Louisiana",
                "Kentucky", "Oregon", "Oklahoma", "Connecticut", "Iowa"
        );

        return states.get(getRandomInt(states.size()));
    }

    private String getRandomCountry() {
        List<String> countries = List.of(
                "United States", "Canada", "Brazil", "Germany", "France",
                "United Kingdom", "Australia", "India", "China", "Japan",
                "Italy", "Spain", "Mexico", "Russia", "South Korea",
                "South Africa", "Netherlands", "Turkey", "Sweden", "Norway",
                "Argentina", "Colombia", "Poland", "Switzerland", "New Zealand",
                "Denmark", "Ireland", "Austria", "Portugal", "Saudi Arabia"
        );

        return countries.get(getRandomInt(countries.size()));
    }

    private String getRandomZipCode() {
        List<String> zipCodes = List.of(
                "10001", "90001", "60601", "77001", "85001",
                "19101", "78201", "92101", "75201", "78701",
                "95101", "76101", "32201", "43201", "28201",
                "46201", "94101", "98101", "80201", "02201",
                "79901", "37201", "48201", "97201", "38101",
                "73101", "89101", "40201", "21201", "53201"
        );

        return zipCodes.get(getRandomInt(zipCodes.size()));
    }

    private String getRandomCategoryName() {
        List<String> categoryNames = List.of(
                "Electronics", "Books", "Clothing", "Sports", "Health & Beauty",
                "Toys", "Home Appliances", "Furniture", "Groceries", "Automotive",
                "Office Supplies", "Jewelry", "Gaming", "Movies & Music", "Travel",
                "Garden", "Fitness", "Pet Supplies", "Crafts", "Baby Products",
                "Footwear", "Tools", "Outdoor Gear", "Accessories", "Luggage",
                "Stationery", "Digital Products", "Luxury Items", "Seasonal", "Hobbies"
        );

        return categoryNames.get(getRandomInt(categoryNames.size()));
    }

    private String getRandomCategoryDescription() {
        List<String> categoryDescriptions = List.of(
                "High-quality electronic gadgets and devices for every need.",
                "A collection of novels, textbooks, and educational material.",
                "Trendy and comfortable clothing for all genders and ages.",
                "Sports equipment and gear for indoor and outdoor activities.",
                "Health and beauty products to look and feel your best.",
                "Fun and engaging toys for kids of all ages.",
                "Durable and reliable home appliances for your daily tasks.",
                "Modern and stylish furniture to enhance your living space.",
                "Fresh and packaged groceries for your everyday essentials.",
                "Automotive parts and accessories to keep your vehicle running.",
                "Essential office supplies for a productive workspace.",
                "Elegant and affordable jewelry for special occasions.",
                "Gaming consoles, accessories, and video games.",
                "Movies, music, and media for your entertainment needs.",
                "Travel accessories and guides for your next adventure.",
                "Tools and decorations for a flourishing garden.",
                "Fitness equipment and gear for a healthier lifestyle.",
                "Pet food, toys, and accessories to pamper your pets.",
                "Crafting materials and supplies for your DIY projects.",
                "Safe and quality products for babies and toddlers.",
                "Footwear for casual, formal, and athletic purposes.",
                "Tools for home improvement and repairs.",
                "Outdoor gear for camping, hiking, and adventure trips.",
                "Stylish accessories to complete your outfits.",
                "Luggage and travel essentials for convenient journeys.",
                "Stationery supplies for school, work, or creative projects.",
                "Digital products like eBooks, software, and online courses.",
                "Premium luxury items for a sophisticated lifestyle.",
                "Seasonal decorations and products for holidays and events.",
                "Everything you need to pursue your favorite hobbies."
        );

        return categoryDescriptions.get(getRandomInt(categoryDescriptions.size()));
    }

    private int getRandomNumber() {
        return getRandomInt(10000);
    }

    private String getRandomNumberString() {
        return String.valueOf(getRandomInt(10000));
    }

    private String getRandomCouponCode() {
        List<String> couponCodes = List.of(
                "SAVE10", "WELCOME15", "FREESHIP", "DISCOUNT20", "SUMMER5",
                "SPRING25", "FIRSTBUY", "DEAL30", "HOLIDAY50", "FLASHSALE",
                "BUNDLE10", "SAVE15NOW", "EXCLUSIVE25", "VIPDEAL40", "FALLSAVE10",
                "WINTER20", "CYBERMONDAY", "BLACKFRIDAY10", "LOYALTY5", "REFER50",
                "SHOPMORE30", "LIMITED15", "FESTIVE10", "NEWYEAR25", "GRAB20",
                "EARLYBIRD5", "EXTRASALE15", "BESTDEAL20", "LUCKY30", "FREESHIP100"
        );

        return couponCodes.get(getRandomInt(couponCodes.size()));
    }

    private String getRandomCouponDescription() {
        List<String> couponDescriptions = List.of(
                "Get 10% off on your next purchase.", "Enjoy 15% off as a welcome gift.",
                "Free shipping on all orders above $50.", "Flat 20% discount on selected items.",
                "Save an extra 5% this summer season.", "Avail 25% off during the spring sale.",
                "First-time buyers get a special discount.", "Up to 30% off on bundled products.",
                "Flat 50% discount on holiday sales.", "Grab huge savings during flash sales.",
                "10% off on product bundles.", "Save 15% instantly on your next purchase.",
                "Enjoy 25% off on exclusive products.", "Get 40% off for VIP members.",
                "Save 10% this fall season.", "Extra 20% off winter collections.",
                "Special discounts for Cyber Monday.", "10% off during Black Friday deals.",
                "5% cashback for loyal customers.", "Refer a friend and get $50 discount.",
                "Get up to 30% off by shopping more.", "Limited-time offer of 15% discount.",
                "10% off festive products.", "Celebrate the new year with 25% off.",
                "Enjoy a flat 20% discount today.", "5% off for early bird purchases.",
                "Extra 15% savings during the sale.", "Flat 20% discount on best deals.",
                "Save 30% with a lucky purchase.", "Enjoy free shipping with no minimum purchase."
        );

        return couponDescriptions.get(getRandomInt(couponDescriptions.size()));
    }

    private String getRandomDiscountType() {
        List<String> discountTypes = List.of(
                "Percentage", "Amount"
        );

        return discountTypes.get(getRandomInt(discountTypes.size()));
    }

    private LocalDateTime getRandomDateTime() {
        List<LocalDateTime> dateTimes = List.of(
                LocalDateTime.of(2025, 4, 15, 23, 59),
                LocalDateTime.of(2025, 5, 30, 23, 59),
                LocalDateTime.of(2025, 6, 10, 23, 59),
                LocalDateTime.of(2025, 7, 1, 23, 59),
                LocalDateTime.of(2025, 8, 15, 23, 59),
                LocalDateTime.of(2025, 9, 25, 23, 59),
                LocalDateTime.of(2025, 10, 5, 23, 59),
                LocalDateTime.of(2025, 11, 11, 23, 59),
                LocalDateTime.of(2025, 12, 24, 23, 59),
                LocalDateTime.of(2025, 1, 1, 23, 59),
                LocalDateTime.of(2025, 2, 14, 23, 59),
                LocalDateTime.of(2025, 3, 8, 23, 59),
                LocalDateTime.of(2025, 6, 21, 23, 59),
                LocalDateTime.of(2025, 7, 4, 23, 59),
                LocalDateTime.of(2025, 9, 15, 23, 59),
                LocalDateTime.of(2025, 10, 31, 23, 59),
                LocalDateTime.of(2025, 11, 27, 23, 59),
                LocalDateTime.of(2025, 12, 25, 23, 59),
                LocalDateTime.of(2025, 4, 10, 23, 59),
                LocalDateTime.of(2025, 5, 20, 23, 59),
                LocalDateTime.of(2025, 6, 30, 23, 59),
                LocalDateTime.of(2025, 7, 18, 23, 59),
                LocalDateTime.of(2025, 8, 12, 23, 59),
                LocalDateTime.of(2025, 9, 1, 23, 59),
                LocalDateTime.of(2025, 10, 25, 23, 59),
                LocalDateTime.of(2025, 11, 5, 23, 59),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                LocalDateTime.of(2025, 1, 15, 23, 59),
                LocalDateTime.of(2025, 2, 28, 23, 59),
                LocalDateTime.of(2025, 3, 31, 23, 59)
        );

        return dateTimes.get(getRandomInt(dateTimes.size()));
    }

    private String getProductName() {
        List<String> productNames = List.of(
                "SmartPhone X1", "UltraHD TV Pro", "EcoSmart Blender", "NoiseCancelling Headphones",
                "Wireless Gaming Mouse", "Digital Fitness Tracker", "Stainless Steel Water Bottle",
                "Ergonomic Office Chair", "Action Camera", "Electric Toothbrush Plus",
                "Multi-Purpose Tool Kit", "Portable Bluetooth Speaker", "Luxury Scented Candle",
                "Adjustable Standing Desk", "Smart Home Security System", "Eco-Friendly Yoga Mat",
                "Compact Travel Backpack", "Smart LED Light Bulb", "Durable Hiking Boots",
                "Wireless Earbuds Max", "Professional Hair Dryer", "High-Speed Blender",
                "Gaming Laptop Turbo", "Stylish Wristwatch", "Electric Scooter Lite",
                "Eco-Friendly Lunch Box", "Stainless Steel Cookware", "Cordless Vacuum Cleaner",
                "Noise-Reducing Earplugs", "Premium Coffee Maker"
        );

        return productNames.get(getRandomInt(productNames.size()));
    }

    private String getProductDescription() {
        List<String> productDescriptions = List.of(
                "Experience cutting-edge technology with a sleek design and fast performance.",
                "Immerse yourself in vivid colors and crystal-clear visuals with this 4K UltraHD TV.",
                "Blend your favorite smoothies and sauces with ease using this eco-friendly blender.",
                "Enjoy uninterrupted sound with premium noise-cancelling technology.",
                "Achieve precision and comfort with this state-of-the-art wireless gaming mouse.",
                "Track your fitness goals with a stylish and accurate digital tracker.",
                "Stay hydrated on the go with this durable, BPA-free stainless steel water bottle.",
                "Improve posture and comfort during long work hours with this ergonomic chair.",
                "Capture every adventure in stunning 4K resolution with this compact action camera.",
                "Upgrade your dental hygiene routine with advanced cleaning technology.",
                "Handle every DIY project with this versatile multi-purpose tool kit.",
                "Take your music anywhere with a powerful and compact Bluetooth speaker.",
                "Create a calming ambiance with luxurious, long-lasting scented candles.",
                "Work comfortably with an adjustable standing desk designed for productivity.",
                "Secure your home with smart monitoring features and easy installation.",
                "Enhance your yoga practice with a lightweight, eco-friendly yoga mat.",
                "Travel light and organized with this durable, compact travel backpack.",
                "Illuminate your home with energy-efficient smart LED bulbs.",
                "Tackle any terrain with these durable, weather-resistant hiking boots.",
                "Experience superior sound quality with the latest in wireless earbuds.",
                "Achieve salon-quality results with this professional hair dryer.",
                "Blend, chop, and puree with this powerful, high-speed blender.",
                "Level up your gaming experience with a high-performance gaming laptop.",
                "Add a touch of elegance with this sleek and stylish wristwatch.",
                "Navigate urban commutes effortlessly with this lightweight electric scooter.",
                "Pack healthy meals with this reusable, eco-friendly lunch box.",
                "Cook with confidence using premium-grade stainless steel cookware.",
                "Clean your home with ease using a lightweight, cordless vacuum cleaner.",
                "Block out distractions with effective and comfortable earplugs.",
                "Brew barista-quality coffee at home with this premium coffee maker."
        );

        return productDescriptions.get(getRandomInt(productDescriptions.size()));
    }

    private String getRandomImageUrl() {
        return "https://example.com/images/img" + getRandomInt(10000) + ".png";
    }

    private double getRandomValue() {
        Random random = new Random();
        return random.nextDouble(10000);
    }

    private RoleDTO getRoleDTO() {
        return new RoleDTO(20, null, null,
                null, null, null, 0);
    }

    public UserDTO getUserDTO() {

        AddressDTO addressDTO = new AddressDTO(0, null, getRandomStreetAddress(),
                getRandomNumberString(), "Home", getRandomCity(), getRandomState(),
                getRandomZipCode(), getRandomCountry(), true, null, null, 0);

        UserDTO userDTO = new UserDTO(0, getRandomUsername(), null, null,
                getRandomEmail(), getRandomName(), getRandomLastName(), getRandomPhone(),
                true, getRoleDTO(), List.of(addressDTO), null, null, 0);

        addressDTO.setUser(userDTO);
        return userDTO;
    }

    public UserDTO getUserDTOWithId(int id) {

        AddressDTO addressDTO = new AddressDTO(0, null, getRandomStreetAddress(),
                getRandomNumberString(), "Home", getRandomCity(), getRandomState(),
                getRandomZipCode(), getRandomCountry(), true, null, null, 0);

        UserDTO userDTO = new UserDTO(id, getRandomUsername(), null, null,
                getRandomEmail(), getRandomName(), getRandomLastName(), getRandomPhone(),
                true, getRoleDTO(), List.of(addressDTO), null, null, 0);

        addressDTO.setUser(userDTO);
        return userDTO;
    }

    public AddressDTO getAddressDTO(UserDTO userDTO) {

        return new AddressDTO(0, userDTO, getRandomStreetAddress(),
                getRandomNumberString(), "Home", getRandomCity(), getRandomState(),
                getRandomZipCode(), getRandomCountry(), true, null, null, 0);
    }

    public AddressDTO getAddressDTOWithId(UserDTO userDTO, int id) {

        return new AddressDTO(id, userDTO, getRandomStreetAddress(),
                getRandomNumberString(), "Work", getRandomCity(), getRandomState(),
                getRandomZipCode(), getRandomCountry(), true, null, null, 0);
    }

    public CategoryDTO getCategoryDTO() {
        return new CategoryDTO(0, getRandomCategoryName(), getRandomCategoryDescription(),
                true, null, null, 0);
    }

    public CategoryDTO getCategoryDTOWithId(int id) {
        return new CategoryDTO(id, getRandomCategoryName(), getRandomCategoryDescription(),
                true, null, null, 0);
    }

    public CouponDTO getCouponDTO() {
        return new CouponDTO(0, getRandomCouponCode(), getRandomCouponDescription(),
                getRandomDiscountType(), getRandomInt(100), getRandomDateTime(),
                getRandomInt(999999), null, 0);
    }

    public ProductDTO getProductDTO() {
        return new ProductDTO(0, getProductName(), getProductDescription(),
                getRandomValue(), getRandomInt(1000), true, List.of(getCategoryDTO()), getProductImageDto(), null, null, 0);
    }

    public ProductDTO getProductDTOWithId(int id) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO(0, "Category Name", "Category Description",
                true, null, null, 0);
        categoryDTOS.add(categoryDTO);



        return new ProductDTO(id, getProductName(), getProductDescription(),
                getRandomValue(), getRandomInt(1000), true, categoryDTOS, getProductImageDto(), null, null, 0);
    }

    public List<ProductImageDTO> getProductImageDto() {
        List<ProductImageDTO> productImageDTOS = new ArrayList<>();
        ProductImageDTO productImageDTO = new ProductImageDTO(0,
                "https://imagesemxaple.com/image.png", true, 0);
        productImageDTOS.add(productImageDTO);
        return productImageDTOS;
    }

    public OrderDetailDTO getOrderDetail(OrderDTO orderDTO, ProductDTO productDTO) {
        return new OrderDetailDTO(0, orderDTO, productDTO,
                1, productDTO.getPrice(), 0);
    }

    public OrderDTO getOrder(ProductDTO productDTO, AddressDTO addressDTO, int userId) {

        OrderDTO orderDTO = new OrderDTO(0, userId, null, 0,
                OrderStatus.PENDING_PAYMENT.getStatus(), List.of(), addressDTO, addressDTO,
                null, null, null, 0);

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(0, orderDTO, productDTO,
                3, productDTO.getPrice(), 0);

        orderDTO.setOrderDetails(List.of(orderDetailDTO));

        return orderDTO;
    }

    public OrderDTO getOrderWithCoupon(ProductDTO productDTO, AddressDTO addressDTO, int userId, CouponDTO couponDTO) {

        OrderDTO orderDTO = new OrderDTO(0, userId, null, 0,
                OrderStatus.PENDING_PAYMENT.getStatus(), List.of(), addressDTO, addressDTO,
                null, couponDTO, null, 0);

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(0, orderDTO, productDTO,
                3, productDTO.getPrice(), 0);

        orderDTO.setOrderDetails(List.of(orderDetailDTO));

        return orderDTO;
    }
}
