# Address Book UI - Jakarta Faces Application

A modern address book user interface built with **Jakarta Faces 4.0**, **Java 17**, and **Tailwind CSS** for managing contacts with full CRUD operations.

## 🚀 Features

- **Modern UI Design**: Clean, responsive interface using Tailwind CSS
- **Jakarta Faces Integration**: Server-side processing with JSF components
- **Full CRUD Operations**: Create, Read, Update, Delete contacts
- **Search & Filter**: Search by name, email, company with category filtering
- **Form Validation**: Client and server-side validation
- **API Integration**: REST API calls to `/api/addressbook` endpoints
- **Responsive Design**: Mobile-friendly layout

## 📋 Contact Fields

- **First Name** *(required)*
- **Last Name** *(required)*
- **Email Address** *(required)*
- Phone Number
- Company
- Category (Personal, Business, Family, Friend)
- Address

## 🛠 Technology Stack

- **Frontend**: Jakarta Faces 4.0.8, XHTML, Tailwind CSS
- **Backend**: Java 17, Jakarta CDI, Jakarta Bean Validation
- **Build Tool**: Maven 3.9+
- **Server**: Jakarta EE compatible server (e.g., WildFly, Payara, TomEE)
- **HTTP Client**: Apache HttpClient 5 for API communication

## 📁 Project Structure

```
addressbook-ui/
├── src/main/java/com/ibm/test/addressbook/
│   ├── model/
│   │   └── AddressBook.java           # Entity model with validation
│   ├── service/
│   │   └── AddressBookService.java    # API service layer
│   └── controller/
│       └── AddressBookController.java # JSF managed bean
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml                    # Web application configuration
│   │   ├── faces-config.xml           # Jakarta Faces configuration
│   │   └── beans.xml                  # CDI configuration
│   ├── index.xhtml                    # Main JSF page
│   └── demo.html                      # Functional demo (development)
├── pom.xml                            # Maven dependencies
└── README.md
```

## 🚦 Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Jakarta EE compatible application server

### Building the Application

```bash
# Clone the repository
git clone <repository-url>
cd addressbook-ui

# Build the WAR file
mvn clean package

# The WAR file will be generated at target/addressbook-ui.war
```

### Deployment

1. **Deploy to Application Server**:
   - Copy `target/addressbook-ui.war` to your server's deployment directory
   - The application will be available at: `http://localhost:8080/addressbook-ui`

2. **Development with Jetty** (experimental):
   ```bash
   mvn jetty:run
   # Access at: http://localhost:8080/addressbook
   ```

### API Configuration

The application expects a REST API server running at `http://localhost:8081/api/addressbook` with the following endpoints:

- `GET /api/addressbook` - List all contacts
- `GET /api/addressbook/{id}` - Get contact by ID
- `POST /api/addressbook` - Create new contact
- `PUT /api/addressbook/{id}` - Update contact
- `DELETE /api/addressbook/{id}` - Delete contact

## 📱 UI Demo

You can view a functional demo of the UI by opening `demo.html` in a web browser. This static HTML version includes:

- ✅ **Full form functionality** with validation
- ✅ **Interactive contact management** (add, edit, delete)
- ✅ **Search and filtering** capabilities
- ✅ **Responsive design** with Tailwind CSS
- ✅ **Success/error messaging**

![Address Book Demo](https://github.com/user-attachments/assets/d9ed6937-a8f6-4417-97f0-58adcb783325)

## 🔧 Configuration

### API Endpoint Configuration

Update the API base URL in `AddressBookService.java`:

```java
private static final String API_BASE_URL = "http://your-api-server:port/api/addressbook";
```

### Server Configuration

The application is configured for Jakarta EE 10 with:

- **Jakarta Faces 4.0**: Web UI framework
- **Jakarta CDI 4.0**: Dependency injection
- **Jakarta Bean Validation 3.0**: Form validation
- **Jakarta Servlet 6.0**: Web layer

## 🧪 Development

### Running Tests

```bash
mvn test
```

### Code Structure

- **Model Layer**: `AddressBook.java` - Entity with validation annotations
- **Service Layer**: `AddressBookService.java` - HTTP client for API calls
- **Controller Layer**: `AddressBookController.java` - JSF managed bean
- **View Layer**: `index.xhtml` - Jakarta Faces XHTML template

### Key Features Implementation

1. **Form Validation**: Uses Jakarta Bean Validation with custom messages
2. **AJAX Support**: JSF AJAX for smooth user interactions
3. **Error Handling**: Comprehensive error handling with user-friendly messages
4. **Responsive Design**: Tailwind CSS grid system for mobile compatibility

## 📄 License

This project is part of the addressbook application suite.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

---

**Base Package**: `com.ibm.test.addressbook`  
**Context Path**: `/addressbook`  
**Main Page**: `index.xhtml`