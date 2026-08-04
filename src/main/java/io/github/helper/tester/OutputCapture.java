package io.github.helper.tester;

import io.github.helper.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Gestisce la redirezione di System.out durante l'esecuzione dei test.
 * <p>
 * Ogni istanza di questa classe rappresenta una "sessione" di cattura indipendente:
 * memorizza lo stream originale (per poterlo ripristinare a fine sessione), uno stream
 * che scarta l'output (usato per i test segreti, che non devono stampare nulla a video)
 * e un buffer che accumula ciò che il codice testato produce, per poterlo confrontare
 * con l'output atteso.
 */
public class OutputCapture {

    private final PrintStream originalOut;
    private final PrintStream consoleOut;
    private final PrintStream silentOut;
    private final ByteArrayOutputStream capturedStream;

    private boolean active = false;

    public OutputCapture() {
        this.originalOut = System.out;
        this.consoleOut = originalOut;
        this.capturedStream = new ByteArrayOutputStream();
        this.silentOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Scarta volutamente l'output a video (usato per i test segreti)
            }
        });
    }

    /** Avvia la cattura mostrando l'output a console (modalità non segreta di default). */
    public void start() {
        active = true;
        redirect(false);
    }

    /**
     * Aggiorna la destinazione di System.out in base al fatto che il test corrente
     * sia segreto (output scartato a video) o pubblico (output visibile).
     * In entrambi i casi, ciò che viene scritto continua a confluire in capturedStream.
     */
    public void redirect(boolean secret) {
        if (!active) {
            throw new IllegalStateException("OutputCapture non avviata: chiama start() prima di redirect()");
        }
        OutputStream activeConsole = secret ? silentOut : consoleOut;
        TeeOutputStream tee = new TeeOutputStream(activeConsole, capturedStream);
        System.setOut(new PrintStream(tee, true, StandardCharsets.UTF_8));
    }

    /** Svuota il buffer di cattura, in preparazione al prossimo test. */
    public void reset() {
        capturedStream.reset();
    }

    /** Ritorna ciò che è stato scritto su System.out da quando è stato chiamato reset(). */
    public String getCapturedOutput() {
        return capturedStream.toString(StandardCharsets.UTF_8).trim();
    }

    /** Ripristina lo System.out originale. Da chiamare sempre a fine sessione (es. in un finally). */
    public void restore() {
        System.setOut(originalOut);
        active = false;
    }
}