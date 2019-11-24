import model.BankClient;
import org.junit.Before;
import org.junit.Test;
import service.BankClientService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DBBankClientTest {

    private BankClientService service;

    @Before
    public void init() throws Exception {
        service = new BankClientService();
        service.cleanUp();
        service.createTable();
    }

    @Test
    public void testEmptyDatabase() throws Exception {
        boolean empty = service.getAllClient().isEmpty();
        assertTrue(empty);
    }

    @Test
    public void testGetAllClient() throws Exception {
        BankClient client1 = new BankClient("a", "a", 100L);
        BankClient client2 = new BankClient("b", "b", 100L);
        BankClient client3 = new BankClient("c", "c", 100L);
        BankClient client4 = new BankClient("d", "d", 100L);

        service.addClient(client1);
        service.addClient(client2);
        service.addClient(client3);
        service.addClient(client4);

        List<BankClient> clients = service.getAllClient();
        assertEquals(clients, Arrays.asList(client1, client2, client3, client4));
    }

    @Test
    public void testAddOneClient() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        service.addClient(client);

        long size = service.getAllClient().size();
        assertEquals(1, size);
    }

    @Test
    public void testAddTwoClient() throws Exception {
        BankClient client1 = new BankClient("a", "a", 100L);
        BankClient client2 = new BankClient("b", "b", 100L);

        service.addClient(client1);
        service.addClient(client2);

        long size = service.getAllClient().size();
        assertEquals(2, size);
    }

    @Test
    public void testAddTwoIdentical() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        assertTrue(service.addClient(client));
        assertFalse(service.addClient(client));

        long size = service.getAllClient().size();
        assertEquals(1, size);
    }

    @Test
    public void testDeleteExistsClient() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        service.addClient(client);
        boolean removed = service.deleteClient(client.getName());
        assertTrue(removed);

        long size = service.getAllClient().size();
        assertEquals(0, size);
    }

    @Test
    public void testDeleteNotExistsClient() throws Exception {
        boolean removed = service.deleteClient("fake");
        assertFalse(removed);
    }

    @Test
    public void testGetExistsClientById() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        service.addClient(client);
        BankClient thatClient = service.getClientById(1);

        assertEquals(client, thatClient);
    }

    @Test
    public void testGetNotExistsClientById() throws Exception {
        BankClient thatClient = service.getClientById(1);
        assertNull(thatClient);
    }

    @Test
    public void testValidateClient() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        service.addClient(client);

        assertTrue(service.validateClient(client.getName(), client.getPassword()));
        assertFalse(service.validateClient(client.getName(), "fake"));
    }

    @Test
    public void testClientHasSum() throws Exception {
        BankClient client = new BankClient("a", "a", 100L);

        service.addClient(client);

        assertTrue(service.isClientHasSum("a", 0L));
        assertTrue(service.isClientHasSum("a", 50L));
        assertTrue(service.isClientHasSum("a", 100L));
        assertFalse(service.isClientHasSum("a", 101L));
        assertFalse(service.isClientHasSum("a", 150L));
    }

    @Test
    public void testTransferMoneyToExistingClient() throws Exception {
        BankClient client1 = new BankClient("a", "a", 100L);
        BankClient client2 = new BankClient("b", "b", 0L);

        service.addClient(client1);
        service.addClient(client2);

        assertTrue(service.sendMoneyToClient(client1, client2.getName(), 50L));
        assertEquals(50L, (long) service.getClientByName(client1.getName()).getMoney());
        assertEquals(50L, (long) service.getClientByName(client2.getName()).getMoney());

        assertTrue(service.sendMoneyToClient(client1, client2.getName(), 50L));
        assertEquals(0L, (long) service.getClientByName(client1.getName()).getMoney());
        assertEquals(100L, (long) service.getClientByName(client2.getName()).getMoney());

        assertFalse(service.sendMoneyToClient(client1, client2.getName(), 50L));
    }
}