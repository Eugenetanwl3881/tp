package seedu.foodrem.logic.commands.itemcommands;

import static java.util.Objects.requireNonNull;
import static seedu.foodrem.logic.parser.CliSyntax.PREFIX_ITEM_QUANTITY;
import static seedu.foodrem.model.Model.PREDICATE_SHOW_ALL_ITEMS;

import java.util.List;

import seedu.foodrem.commons.core.Messages;
import seedu.foodrem.commons.core.index.Index;
import seedu.foodrem.logic.commands.Command;
import seedu.foodrem.logic.commands.CommandResult;
import seedu.foodrem.logic.commands.exceptions.CommandException;
import seedu.foodrem.model.Model;
import seedu.foodrem.model.item.Item;
import seedu.foodrem.model.item.ItemQuantity;

/**
 * Increments the quantity of an item by a specified amount.
 */
public class DecrementCommand extends Command {
    public static final String COMMAND_WORD = "dec";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Decrements the quantity of the item identified by the index number used in the displayed item list.\n"
            + "If a quantity is not provided, the item quantity will be decremented by 1. \n"
            + "Parameters:\n"
            + "INDEX (must be a positive integer) [" + PREFIX_ITEM_QUANTITY + "QUANTITY]\n"
            + "Example:\n"
            + COMMAND_WORD + " 10\n"
            + COMMAND_WORD + " 10 " + PREFIX_ITEM_QUANTITY + "100";

    public static final String MESSAGE_EDIT_ITEM_SUCCESS = "Decremented Item: %1$s";
    private final Index index;
    private final ItemQuantity quantity;

    /**
     * @param index    of the item in the filtered item list to increment.
     * @param quantity quantity of the item to increment.
     */
    public DecrementCommand(Index index, ItemQuantity quantity) {
        requireNonNull(index);
        requireNonNull(quantity);

        this.index = index;
        this.quantity = quantity;
    }

    /**
     * Creates and returns a {@code Item} with the quantity of {@code itemToEdit}
     * decremented by  {@code editItemDescriptor}.
     */
    private static Item createDecrementedItem(Item itemToDecrement, ItemQuantity quantity) {
        assert itemToDecrement != null;

        ItemQuantity decrementedQuantity;
        try {
            decrementedQuantity = ItemQuantity.performArithmeticOperation(
                    itemToDecrement.getQuantity(), quantity, (x, y) -> x - y);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("(Final Quantity) " + e.getMessage());
        }

        return new Item(itemToDecrement.getName(),
                decrementedQuantity,
                itemToDecrement.getUnit(),
                itemToDecrement.getBoughtDate(),
                itemToDecrement.getExpiryDate());
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Item> lastShownList = model.getFilteredItemList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEMS_DISPLAYED_INDEX);
        }

        Item itemToDecrement = lastShownList.get(index.getZeroBased());
        Item decrementedItem = createDecrementedItem(itemToDecrement, quantity);

        model.setItem(itemToDecrement, decrementedItem);
        model.updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);
        return new CommandResult(String.format(MESSAGE_EDIT_ITEM_SUCCESS, decrementedItem));
    }
}