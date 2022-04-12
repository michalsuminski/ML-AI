package org.jpms.text_operators;


import org.jpms.features.Document;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.lucene.util.IOUtils;


/**
 * Split the Reuters SGML documents into Text files containing: Title, Date, Place, Body
 */
public class FileOperator {
    private File reutersDir;
    private File outputDir;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public FileOperator(File reutersDir, File outputDir) {
        this.reutersDir = reutersDir;
        this.outputDir = outputDir;
        System.out.println("Deleting all files in " + outputDir);
        for (File f : outputDir.listFiles()) {
            f.delete();
        }
    }

    public void extract() {
        File[] sgmFiles = reutersDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".sgm");
            }
        });
        if (sgmFiles != null && sgmFiles.length > 0) {
            for (File sgmFile : sgmFiles) {
                extractFile(sgmFile);
            }
        } else {
            System.err.println("No .sgm files in " + reutersDir);
        }
    }

    Pattern EXTRACTION_PATTERN = Pattern
//            .compile("<TITLE>(.*?)</TITLE>|<DATE>(.*?)</DATE>|<PLACES>(.*?)</PLACES>|<BODY>(.*?)</BODY>");
            .compile("<PLACES>(.*?)</PLACES>|<BODY>(.*?)</BODY>");

    private static String[] META_CHARS = {"&", "<", ">", "\"", "'"};

    private static String[] META_CHARS_SERIALIZATIONS = {"&amp;", "&lt;",
            "&gt;", "&quot;", "&apos;"};

    protected void extractFile(File sgmFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sgmFile)));

            StringBuilder buffer = new StringBuilder(1024);
//            StringBuilder[] sb = new StringBuilder[13766];
            StringBuilder outBuffer = new StringBuilder(1024);
            String line = null;
            int docNumber = 0;
            boolean fileToConvert = true;
            while ((line = reader.readLine()) != null) {
                // when we see a closing reuters tag, flush the file

                if (line.indexOf("</REUTERS") == -1) {
                    // Replace the SGM escape sequences

                    buffer.append(line).append(' ');// accumulate the strings for now,
                    // then apply regular expression to
                    // get the pieces,
                } else {
                    // Extract the relevant pieces and write to a file in the output dir
                    Matcher matcher = EXTRACTION_PATTERN.matcher(buffer);
                    while (matcher.find()) {
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            if (matcher.group(i) != null) {
//                                System.out.println(i + " " + matcher.group(i));
                                if (i == 1) {
                                    switch (matcher.group(i)) {
                                        case "<D>usa</D>":
                                            outBuffer.append("usa");
                                            break;
                                        case "<D>france</D>":
                                            outBuffer.append("france");
                                            break;
                                        case "<D>canada</D>":
                                            outBuffer.append("canada");
                                            break;
                                        case "<D>uk</D>":
                                            outBuffer.append("uk");
                                            break;
                                        case "<D>japan</D>":
                                            outBuffer.append("japan");
                                            break;
                                        case "<D>west-germany</D>":
                                            outBuffer.append("west-germany");
                                            break;
                                        default:
                                            fileToConvert = false;
                                            outBuffer.append(" ");
                                    }
                                } else {
                                    outBuffer.append(matcher.group(i));
                                }
//                                outBuffer.append(matcher.group(i));
                            }
                        }
                        outBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
                    }
                    if (fileToConvert) {
                        String out = outBuffer.toString();
                        for (int i = 0; i < META_CHARS_SERIALIZATIONS.length; i++) {
                            out = out.replaceAll(META_CHARS_SERIALIZATIONS[i], META_CHARS[i]);
                        }
                        File outFile = new File(outputDir, sgmFile.getName() + "-"
                                + (docNumber++) + ".txt");
                        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile));
                        writer.write(out);
                        writer.close();
                    }
                    outBuffer.setLength(0);
                    buffer.setLength(0);
                }
                fileToConvert = true;
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        ArrayList<String> txt = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    txt.add(line);
                }
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return txt;
    }

    public static void createOneFileWithReutersArticles() throws IOException {
        File folder = new File(".\\txt\\");
        File[] listOfFiles;//get names of all files extracted from .sgm files
        listOfFiles = folder.listFiles();
        ArrayList<ArrayList<String>> articles = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) { //read all files extracted from .sgm files
            if (FileOperator.readFile(String.valueOf(listOfFiles[i])).size() == 1) {
                continue; //there are a couple of files that don't have body inside, we removed them
            }
            articles.add(FileOperator.readFile(String.valueOf(listOfFiles[i])));
        }
        FileWriter writer = new FileWriter("reuters_articles.txt"); //all articles write to file reuters_articles.txt
        for (ArrayList<String> str : articles) {
            for (String line : str
            ) {
                writer.write(line + System.lineSeparator());
            }

        }
        writer.close();
    }

    public static ArrayList<ArrayList<String>> getFeatureDictionary(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        ArrayList<String> wordsFromOneCountry;
        ArrayList<ArrayList<String>> featureDictionary = new ArrayList<ArrayList<String>>();
        TextCleaner tc = new TextCleaner();
        ArrayList<String> stemmedWordsFromOneCountry;
        try {
            String line = br.readLine();
            while (line != null) {
                wordsFromOneCountry = new ArrayList(Arrays.asList(line.split(",")));
                stemmedWordsFromOneCountry = new ArrayList<>();
                for(int i=0; i<wordsFromOneCountry.size(); i++){
                    String txt = String.join(" ", tc.cleanAndStem(wordsFromOneCountry.get(i)));
                    stemmedWordsFromOneCountry.add(txt);
                }
                featureDictionary.add(stemmedWordsFromOneCountry);
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return featureDictionary;
    }

    public static void main(String[] args) {
        Path path1 = Paths.get("src/main/resources/articles");
        File reutersDir = new File(String.valueOf(path1));
        if (!reutersDir.exists()) {
            System.out.println("Cannot find Path to Reuters SGM files (" + reutersDir + ")");
            return;
        }

        // First, extract to a tmp directory and only if everything succeeds, rename
        // to output directory.
        File outputDir = new File(".\\txt");
        outputDir = new File(outputDir.getAbsolutePath() + "-tmp");
        outputDir.mkdirs();
        FileOperator extractor = new FileOperator(reutersDir, outputDir);
        extractor.extract();
        // Now rename to requested output dir
        outputDir.renameTo(new File(".\\txt"));
    }
    public static ArrayList<Document> getNDocuments(int numberOfDocuments) throws IOException {
        ArrayList<String> articles = FileOperator.readFile("src/main/resources/articles/reuters_articles.txt");
        ArrayList<Document> documents = new ArrayList<>();
        for (int i = 0; i < numberOfDocuments*2; i+=2) {
            documents.add(new Document(new ArrayList<>(Arrays.asList(articles.get(i), articles.get(i + 1)))));
        }
        return documents;
    }


}