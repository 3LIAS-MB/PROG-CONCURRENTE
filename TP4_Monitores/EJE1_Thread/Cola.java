package EJE1_Thread;

interface Cola {
    void producir(int producto, int idProductor) throws InterruptedException;
    int consumir(int idConsumidor) throws InterruptedException;
    int getTamanio();
}
