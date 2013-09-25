package org.uli.tcpmon;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Main {
    static private final String NAME = "uli-tcpmon";
    Logger logger = LoggerFactory.getLogger(Main.class);

    static public void main(String[] args) throws Exception {
        Main main = new Main();
        int exitCode = main.execute(args); 
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public int execute(String[] args) throws Exception {
        int exitCode = 0;
        for (;;) {
            int localPort;
            String remoteHost;
            int remotePort;
            MessageFormatterType messageFormatterType = MessageFormatterType.MIXED;
            Options options = new Options();
            Option lp = MyOptionBuilder.init().withArgName("localPort")
                        .hasArg(true)
                        .isRequired(true)
                        .withDescription("local port")
                        .withLongOpt("local-port")
                        .create("l");
            Option rh = MyOptionBuilder.init().withArgName("remoteHost")
                        .hasArg(true)
                        .isRequired(true)
                        .withDescription("remote host")
                        .withLongOpt("remote-host")
                        .create("h");
            Option rp = MyOptionBuilder.init().withArgName("remotePort")
                        .hasArg(true)
                        .isRequired(true)
                        .withDescription("remote port")
                        .withLongOpt("remote-port")
                        .create("r");
            Option textOnly = MyOptionBuilder.init()
                        .hasArg(false)
                        .isRequired(false)
                        .withDescription("do text-only logging")
                        .withLongOpt("text-only")
                        .create("T");
            Option hexOnly = MyOptionBuilder.init()
                        .hasArg(false)
                        .isRequired(false)
                        .withDescription("do hex-only logging")
                        .withLongOpt("hex-only")
                        .create("H");
            options.addOption(lp);
            options.addOption(rh);
            options.addOption(rp);
            options.addOption(textOnly);
            options.addOption(hexOnly);
            CommandLineParser commandLineParser = new PosixParser();
            try {
                CommandLine commandLine = commandLineParser.parse(options, args);
                localPort = Integer.parseInt(commandLine.getOptionValue("l"));
                remoteHost = commandLine.getOptionValue("h");
                remotePort = Integer.parseInt(commandLine.getOptionValue("r"));
                if (commandLine.hasOption("H")) {
                    messageFormatterType = MessageFormatterType.HEX_ONLY;
                }
                if (commandLine.hasOption("T")) {
                    messageFormatterType = MessageFormatterType.TEXT_ONLY;
                }
            } catch (ParseException e) {
                System.err.println(NAME + ": Command line error - " + e.getMessage());
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(NAME, options);
                exitCode = 1;
                break;
            }
            logger.info("Proxying *:{} to {}:{}", localPort, remoteHost, remotePort);
            MessageFormatter formatter = MessageFormatterFactory.getMessageFormatter(messageFormatterType);
            // Configure the bootstrap.
            Executor executor = Executors.newCachedThreadPool();
            ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));
            // Set up the event pipeline factory.
            ClientSocketChannelFactory cf = new NioClientSocketChannelFactory(executor, executor);
            sb.setPipelineFactory(new LoggingProxyPipelineFactory(cf, remoteHost, remotePort, formatter));
            // Start up the server.
            sb.bind(new InetSocketAddress(localPort));
            break;
        }
        return exitCode; // is this ever reached?
    }
}
