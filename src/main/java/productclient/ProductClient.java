package productclient;

import java.util.Scanner;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import org.glassfish.jersey.jackson.JacksonFeature;

public class ProductClient {

    private static final String BASE_URI ="http://localhost:8080/Product/api/";

    private static Client client;
    private static WebTarget target;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

    	client = ClientBuilder.newClient();

    	client.register(JacksonFeature.class);

    	target = client.target(BASE_URI).path("products");

        target = client.target(BASE_URI).path("products");

        boolean running = true;

        while (running) {

            System.out.println("\n===== PRODUCT CLIENT =====");
            System.out.println("1. Show all products");
            System.out.println("2. Find product");
            System.out.println("3. Add product");
            System.out.println("4. Update product");
            System.out.println("5. Delete product");
            System.out.println("6. Cheapest product");
            System.out.println("7. Most expensive product");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {

                    case "1":
                        getAllProducts();
                        break;

                    case "2":
                        getProduct();
                        break;

                    case "3":
                        addProduct();
                        break;

                    case "4":
                        updateProduct();
                        break;

                    case "5":
                        deleteProduct();
                        break;

                    case "6":
                        getCheapestProduct();
                        break;

                    case "7":
                        getMostExpensiveProduct();
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        client.close();
        scanner.close();

        System.out.println("Client closed.");
    }

    private static void getAllProducts() {

        ProductList products = target
                .request(MediaType.APPLICATION_XML)
                .get(ProductList.class);

        if (products.getProducts().isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        for (Product product : products.getProducts()) {
            System.out.println(product);
        }
    }
    private static void getProduct() {

        System.out.print("Product name: ");
        String name = scanner.nextLine();

        Product product = target
                .path(name)
                .request(MediaType.APPLICATION_JSON)
                .get(Product.class);

        System.out.println(product);
    }

    private static void addProduct() {

        Product product = readProduct();

        Response response = target
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(product, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus() + ": "
                + response.readEntity(String.class));

        response.close();
    }

    private static void updateProduct() {

        System.out.print("Product name to update: ");
        String name = scanner.nextLine();

        Product product = readProduct();

        Response response = target
                .path(name)
                .request(MediaType.TEXT_PLAIN)
                .put(Entity.entity(product, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus() + ": "
                + response.readEntity(String.class));

        response.close();
    }

    private static void deleteProduct() {

        System.out.print("Product name to delete: ");
        String name = scanner.nextLine();

        Response response = target
                .path(name)
                .request(MediaType.TEXT_PLAIN)
                .delete();

        System.out.println(response.getStatus() + ": "
                + response.readEntity(String.class));

        response.close();
    }

    private static void getCheapestProduct() {

        Product product = target
                .path("cheapest")
                .request(MediaType.APPLICATION_XML)
                .get(Product.class);

        System.out.println("Cheapest product: " + product);
    }

    private static void getMostExpensiveProduct() {

        Product product = target
                .path("most-expensive")
                .request(MediaType.APPLICATION_XML)
                .get(Product.class);

        System.out.println("Most expensive product: " + product);
    }

    private static Product readProduct() {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Unit price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();

        System.out.print("Amount: ");
        int amount = Integer.parseInt(scanner.nextLine());

        return new Product(name, price, expiryDate, amount);
    }
}