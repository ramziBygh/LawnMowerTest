package mowitnow.interview.test.service;

import mowitnow.interview.test.model.Lawn;
import mowitnow.interview.test.model.Mower;
import mowitnow.interview.test.model.Position;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MowingServiceImpl implements MowingService {

    private static final String EMPTY_FILE = "the file is empty";
    private static final String DIMENSIONS_EXCEPTION = "Illegal arguments: list of dimensions >2";
    private static final String MISSING_DATA_EXCEPTION = "Illegal Argument: Missed data";
    private static final String UNKNOWN_INSTRUCTION = "Illegal Argument: unknown instruction";
    private static final String NORTH = "N";
    private static final String EAST = "E";
    private static final String SOUTH = "S";
    private static final String WEST = "W";

    @Override
    public String moveTheMower(MultipartFile instructionsFile) throws Exception {

        if (instructionsFile.isEmpty()) {
            throw new Exception(EMPTY_FILE);
        } else {

            //read data from the instructions file
            List<String> lines = parseInstructionsFile(instructionsFile);

            //create lawn
            Lawn lawn = getLawnDimensions(lines.get(0));

            //get the mowers initial positions and their instructions
            Map<Mower, char[]> mowersInfosAndInstructionsList = getMowerInfosAndItsListOfInstructions(lines.subList(1, lines.size()));

            //initialize orientations list
            List<String> orientations = Arrays.asList(NORTH, EAST, SOUTH, WEST);

            return executeInstructions(mowersInfosAndInstructionsList, lawn, orientations);
        }
    }

    private List<String> parseInstructionsFile(MultipartFile instructionsFile) throws IOException {

        List<String> lines = new ArrayList<>();
        try {
            String line;
            InputStream inputStream = instructionsFile.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw e;
        }
    }

    private Lawn getLawnDimensions(String lawnDimensionsString) throws Exception {
        String[] lawnDimensionsList = lawnDimensionsString.split(" ");
        if (lawnDimensionsList.length > 2) {
            throw new Exception(DIMENSIONS_EXCEPTION);
        }

        Lawn lawn = new Lawn();
        lawn.setHorizontalLength(Integer.valueOf(lawnDimensionsList[0]));
        lawn.setVerticalLength(Integer.valueOf(lawnDimensionsList[1]));

        return lawn;
    }

    private Map<Mower, char[]> getMowerInfosAndItsListOfInstructions(List<String> lines) throws Exception {

        List<String> listOfMowersInfos = createSubList(lines, 0);
        List<String> listOfInstructions = createSubList(lines, 1);
        if (listOfMowersInfos.size() != listOfInstructions.size()) {
            throw new Exception(MISSING_DATA_EXCEPTION);
        }

        Map<Mower, char[]> mowerInstructionsMap = new LinkedHashMap<>();
        for (int index = 0; index < listOfMowersInfos.size(); index++) {
            String[] mowerInfos = listOfMowersInfos.get(index).split(" ");
            Position position = new Position(Integer.valueOf(mowerInfos[0]), Integer.valueOf(mowerInfos[1]));
            Mower mower = new Mower(position, mowerInfos[2]);
            char[] instructions = listOfInstructions.get(index).toCharArray();
            mowerInstructionsMap.put(mower, instructions);
        }

        return mowerInstructionsMap;
    }

    private List<String> createSubList(List<String> lines, int condition) {
        return IntStream
                .range(0, lines.size())
                .filter(i -> i % 2 == condition)
                .mapToObj(i -> lines.get(i))
                .collect(Collectors.toList());
    }

    private boolean checkMowerIsInsideTheMowingArea(Lawn lawn, Mower mower) {
        return mower.getPosition().getX() <= lawn.getHorizontalLength() && mower.getPosition().getY() <= lawn.getVerticalLength();
    }

    private String executeInstructions(Map<Mower, char[]> mowersInfosAndInstructionsList, Lawn lawn, List<String> orientations) throws Exception {

        String result = "";
        for (Map.Entry<Mower, char[]> mowerInfosAndInstructionsList : mowersInfosAndInstructionsList.entrySet()) {
            Mower mower = mowerInfosAndInstructionsList.getKey();
            char[] instructions = mowerInfosAndInstructionsList.getValue();
                int i = 0;
                while (checkMowerIsInsideTheMowingArea(lawn, mower) && i < instructions.length) {

                    char instruction = instructions[i];
                    switch (instruction) {
                        case 'G': {
                            int index = orientations.indexOf(mower.getDirection());
                            if (index == 0) {
                                mower.setDirection(orientations.get(3));
                            } else {
                                mower.setDirection(orientations.get(index - 1));
                            }
                            break;
                        }
                        case 'D': {
                            int index = orientations.indexOf(mower.getDirection());
                            if (index == 3) {
                                mower.setDirection(orientations.get(0));
                            } else {
                                mower.setDirection(orientations.get(index + 1));
                            }
                            break;
                        }
                        case 'A': {
                            String direction = mower.getDirection();
                            switch (direction) {
                                case NORTH: {
                                    mower.getPosition().setY(mower.getPosition().getY() + 1);
                                    break;
                                }
                                case EAST: {
                                    mower.getPosition().setX(mower.getPosition().getX() + 1);
                                    break;
                                }
                                case SOUTH: {
                                    mower.getPosition().setY(mower.getPosition().getY() - 1);
                                    break;
                                }
                                case WEST: {
                                    mower.getPosition().setX(mower.getPosition().getX() - 1);
                                    break;
                                }
                            }
                            break;
                        }
                        default:
                            throw new Exception(UNKNOWN_INSTRUCTION);
                    }
                    i++;
                }

            String finalPosition = mower.getPosition().getX() + " " + mower.getPosition().getY() + " " + mower.getDirection();
            result += finalPosition + "\n";
            System.out.println(finalPosition);
        }

        return result;
    }
}
