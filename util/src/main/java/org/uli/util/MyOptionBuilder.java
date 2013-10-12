/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uli.util;

import org.apache.commons.cli.Option;

/**
 * MyOptionBuilder is derived from:
 * OptionBuilder allows the user to create Options using descriptive methods.
 *
 * <p>Details on the Builder pattern can be found at
 * <a href="http://c2.com/cgi-bin/wiki?BuilderPattern">
 * http://c2.com/cgi-bin/wiki?BuilderPattern</a>.</p>
 *
 * @author John Keyes (john at integralsource.com)
 * @version $Revision: 754830 $, $Date: 2009-03-16 00:26:44 -0700 (Mon, 16 Mar 2009) $
 * @since 1.0
 *
 * Basically, I'd like to get rid of warnings like these:
 *
 *   .../src/main/java/org/uli/htmlunescape/Main.java:50: warning: [static] static method should be qualified by type name, OptionBuilder, instead of by an expression
 *                    .create("h");
 *
 * You'll get them when you use the OptionBuilder like this:
 *
 *    Option lp = OptionBuilder.withArgName("hostname")
 *                 .hasArg(true)
 *                 .isRequired(true)
 *                 .withDescription("host")
 *                 .withLongOpt("host")
 *                 .create("h");
 *
 * With MyOptionBuilder, you'll do it like this:
 *
 *    Option lp = MyOptionBuilder.init().withArgName("hostname")
 *                 .hasArg(true)
 *                 .isRequired(true)
 *                 .withDescription("host")
 *                 .withLongOpt("host")
 *                 .create("h");
 *
 * There will be no warning. All you have to do is
 *
 *   * use MyOptionBuilder instead of OptionBuilder
 *   * add the init() call
 */
public final class MyOptionBuilder
{
    /** long option */
    private String longopt;

    /** option description */
    private String description;

    /** argument name */
    private String argName;

    /** is required? */
    private boolean required;

    /** the number of arguments */
    private int numberOfArgs = Option.UNINITIALIZED;

    /** option type */
    private Object type;

    /** option can have an optional argument value */
    private boolean optionalArg;

    /** value separator for argument value */
    private char valuesep;

    /** option builder instance */
    private MyOptionBuilder instance = new MyOptionBuilder();

    /**
     * private constructor to prevent instances being created
     */
    private MyOptionBuilder()
    {
        // hide the constructor
    }

    static public MyOptionBuilder init() {
        return new MyOptionBuilder();
    }

    /**
     * Resets the member variables to their default values.
     */
    private void reset()
    {
        description = null;
        argName = "arg";
        longopt = null;
        type = null;
        required = false;
        numberOfArgs = Option.UNINITIALIZED;


        // PMM 9/6/02 - these were missing
        optionalArg = false;
        valuesep = (char) 0;
    }

    /**
     * The next Option created will have the following long option value.
     *
     * @param newLongopt the long option value
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withLongOpt(String newLongopt)
    {
        this.longopt = newLongopt;

        return instance;
    }

    /**
     * The next Option created will require an argument value.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasArg()
    {
        this.numberOfArgs = 1;

        return instance;
    }

    /**
     * The next Option created will require an argument value if
     * <code>hasArg</code> is true.
     *
     * @param hasArg if true then the Option has an argument value
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasArg(boolean hasArg)
    {
        this.numberOfArgs = hasArg ? 1 : Option.UNINITIALIZED;

        return instance;
    }

    /**
     * The next Option created will have the specified argument value name.
     *
     * @param name the name for the argument value
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withArgName(String name)
    {
        this.argName = name;

        return instance;
    }

    /**
     * The next Option created will be required.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder isRequired()
    {
        this.required = true;

        return instance;
    }

    /**
     * The next Option created uses <code>sep</code> as a means to
     * separate argument values.
     *
     * <b>Example:</b>
     * <pre>
     * Option opt = this.withValueSeparator(':')
     *                           .create('D');
     *
     * CommandLine line = parser.parse(args);
     * String propertyName = opt.getValue(0);
     * String propertyValue = opt.getValue(1);
     * </pre>
     *
     * @param sep The value separator to be used for the argument values.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withValueSeparator(char sep)
    {
        this.valuesep = sep;

        return instance;
    }

    /**
     * The next Option created uses '<code>=</code>' as a means to
     * separate argument values.
     *
     * <b>Example:</b>
     * <pre>
     * Option opt = this.withValueSeparator()
     *                           .create('D');
     *
     * CommandLine line = parser.parse(args);
     * String propertyName = opt.getValue(0);
     * String propertyValue = opt.getValue(1);
     * </pre>
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withValueSeparator()
    {
        this.valuesep = '=';

        return instance;
    }

    /**
     * The next Option created will be required if <code>required</code>
     * is true.
     *
     * @param newRequired if true then the Option is required
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder isRequired(boolean newRequired)
    {
        this.required = newRequired;

        return instance;
    }

    /**
     * The next Option created can have unlimited argument values.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasArgs()
    {
        this.numberOfArgs = Option.UNLIMITED_VALUES;

        return instance;
    }

    /**
     * The next Option created can have <code>num</code> argument values.
     *
     * @param num the number of args that the option can have
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasArgs(int num)
    {
        this.numberOfArgs = num;

        return instance;
    }

    /**
     * The next Option can have an optional argument.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasOptionalArg()
    {
        this.numberOfArgs = 1;
        this.optionalArg = true;

        return instance;
    }

    /**
     * The next Option can have an unlimited number of optional arguments.
     *
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasOptionalArgs()
    {
        this.numberOfArgs = Option.UNLIMITED_VALUES;
        this.optionalArg = true;

        return instance;
    }

    /**
     * The next Option can have the specified number of optional arguments.
     *
     * @param numArgs - the maximum number of optional arguments
     * the next Option created can have.
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder hasOptionalArgs(int numArgs)
    {
        this.numberOfArgs = numArgs;
        this.optionalArg = true;

        return instance;
    }

    /**
     * The next Option created will have a value that will be an instance
     * of <code>type</code>.
     *
     * @param newType the type of the Options argument value
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withType(Object newType)
    {
        this.type = newType;

        return instance;
    }

    /**
     * The next Option created will have the specified description
     *
     * @param newDescription a description of the Option's purpose
     * @return the OptionBuilder instance
     */
    public MyOptionBuilder withDescription(String newDescription)
    {
        this.description = newDescription;

        return instance;
    }

    /**
     * Create an Option using the current settings and with
     * the specified Option <code>char</code>.
     *
     * @param opt the character representation of the Option
     * @return the Option instance
     * @throws IllegalArgumentException if <code>opt</code> is not
     * a valid character.  See Option.
     */
    public Option create(char opt) throws IllegalArgumentException
    {
        return create(String.valueOf(opt));
    }

    /**
     * Create an Option using the current settings
     *
     * @return the Option instance
     * @throws IllegalArgumentException if <code>longOpt</code> has not been set.
     */
    public Option create() throws IllegalArgumentException
    {
        if (longopt == null)
        {
            this.reset();
            throw new IllegalArgumentException("must specify longopt");
        }

        return create(null);
    }

    /**
     * Create an Option using the current settings and with
     * the specified Option <code>char</code>.
     *
     * @param opt the <code>java.lang.String</code> representation
     * of the Option
     * @return the Option instance
     * @throws IllegalArgumentException if <code>opt</code> is not
     * a valid character.  See Option.
     */
    public Option create(String opt) throws IllegalArgumentException
    {
        Option option = null;
        try {
            // create the option
            option = new Option(opt, description);

            // set the option properties
            option.setLongOpt(longopt);
            option.setRequired(required);
            option.setOptionalArg(optionalArg);
            option.setArgs(numberOfArgs);
            option.setType(type);
            option.setValueSeparator(valuesep);
            option.setArgName(argName);
        } finally {
            // reset the OptionBuilder properties
            this.reset();
        }

        // return the Option instance
        return option;
    }
}
