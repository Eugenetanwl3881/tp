package seedu.foodrem.logic.commands.statscommands;

import static java.util.Objects.requireNonNull;
import static seedu.foodrem.commons.enums.CommandType.STATS_COMMAND;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.foodrem.logic.commands.Command;
import seedu.foodrem.logic.commands.CommandResult;
import seedu.foodrem.model.Model;
import seedu.foodrem.model.item.Item;
import seedu.foodrem.model.item.itemcomparators.ItemCostComparator;
import seedu.foodrem.model.tag.Tag;
import seedu.foodrem.viewmodels.Stats;

/**
 * Displays FoodRem statistics to the user.
 */
public class StatsCommand extends Command {
    @Override
    public CommandResult<Stats> execute(Model model) {
        requireNonNull(model);
        ObservableList<Item> itemList = model.getFoodRem().getItemList();
        ObservableList<Tag> tagList = model.getFoodRem().getTagList();
        List<Item> expensiveItems = getTopThreeExpensiveItems(itemList);
        List<Tag> commonTags = getTopThreeCommonTags(itemList, tagList);
        double amountWasted = getAmountWasted(itemList);
        return CommandResult.from(new Stats(amountWasted, expensiveItems, commonTags));
    }

    public static String getUsage() {
        return STATS_COMMAND.getUsage();
    }

    private List<Item> getTopThreeExpensiveItems(List<Item> itemList) {
        final List<Item> copy = new ArrayList<>(itemList);
        copy.sort(new ItemCostComparator().reversed());
        return copy.subList(0, 3);
    }

    private double getAmountWasted(List<Item> itemList) {
        return itemList.stream()
                .filter(i -> !i.getQuantity().isZero() && i.getExpiryDate().isAfterExpiryDate(LocalDate.now()))
                .map(i -> i.getPrice().getItemPrice()).reduce(0.0, Double::sum);
    }

    private List<Tag> getTopThreeCommonTags(List<Item> itemList, List<Tag> tagList) {
        HashMap<Tag, Integer> tagToFrequencyMap = new HashMap<>();

        tagList.parallelStream().forEach(tag -> tagToFrequencyMap.put(tag,
                (int) itemList.parallelStream().filter(item -> item.getTagSet().contains(tag)).count()));

        return tagToFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}