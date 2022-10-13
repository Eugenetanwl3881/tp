package seedu.foodrem.logic.commands.tagcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.foodrem.logic.commands.CommandTestUtil.VALID_TAG_NAME_FRUITS;
import static seedu.foodrem.logic.commands.CommandTestUtil.VALID_TAG_NAME_NUMBERS;
import static seedu.foodrem.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodrem.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodrem.testutil.TypicalFoodRem.getTypicalFoodRem;

import org.junit.jupiter.api.Test;

import seedu.foodrem.logic.commands.generalcommands.ResetCommand;
import seedu.foodrem.model.FoodRem;
import seedu.foodrem.model.Model;
import seedu.foodrem.model.ModelManager;
import seedu.foodrem.model.UserPrefs;
import seedu.foodrem.model.tag.Tag;
import seedu.foodrem.testutil.TagBuilder;

public class RenameTagCommandTest {

    private final Model model = new ModelManager(getTypicalFoodRem(), new UserPrefs());

    @Test
    public void execute_renameTag_success() {
        Tag originalTag = new TagBuilder().withTagName(VALID_TAG_NAME_FRUITS).build();
        Tag renamedTag = new TagBuilder().withTagName("test").build();
        RenameTagCommand renameTagCommand = new RenameTagCommand(originalTag, renamedTag);

        String expectedMessage = String.format(RenameTagCommand.MESSAGE_RENAME_TAG_SUCCESS, renamedTag);

        Model expectedModel = new ModelManager(new FoodRem(model.getFoodRem()), new UserPrefs());
        expectedModel.setTag(originalTag, renamedTag);

        assertCommandSuccess(renameTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatedTagName_failure() {
        Tag originalTag = new TagBuilder().withTagName(VALID_TAG_NAME_FRUITS).build();
        Tag renamedTag = new TagBuilder().withTagName(VALID_TAG_NAME_NUMBERS).build();
        RenameTagCommand renameTagCommand = new RenameTagCommand(originalTag, renamedTag);

        assertCommandFailure(renameTagCommand, model, RenameTagCommand.MESSAGE_DUPLICATE_TAG_NAME);
    }

    @Test
    public void execute_tagNotFound_failure() {
        Tag originalTag = new TagBuilder().withTagName("NONE").build();
        Tag renamedTag = new TagBuilder().withTagName("test").build();
        RenameTagCommand renameTagCommand = new RenameTagCommand(originalTag, renamedTag);

        assertCommandFailure(renameTagCommand, model, RenameTagCommand.MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void equals() {
        Tag originalTag = new TagBuilder().withTagName(VALID_TAG_NAME_FRUITS).build();
        Tag renamedTag = new TagBuilder().withTagName("test").build();
        final RenameTagCommand renameTagCommand = new RenameTagCommand(originalTag, renamedTag);

        // same renamedTag -> returns true
        Tag renamedTagCopy = new TagBuilder().withTagName("test").build();
        RenameTagCommand renameTagCommandCopy = new RenameTagCommand(originalTag, renamedTagCopy);
        assertEquals(renameTagCommand, renameTagCommandCopy);

        // same originalTag -> returns true
        Tag originalTagCopy = new TagBuilder().withTagName(VALID_TAG_NAME_FRUITS).build();
        RenameTagCommand renameTagCommandCopy1 = new RenameTagCommand(originalTagCopy, renamedTag);
        assertEquals(renameTagCommand, renameTagCommandCopy1);

        // same object -> returns true
        assertEquals(renameTagCommand, renameTagCommand);

        // null -> returns false
        assertNotEquals(renameTagCommand, null);

        // different types -> returns false
        assertNotEquals(renameTagCommand, new ResetCommand());

        // different index -> returns false
        Tag differentTag = new TagBuilder().withTagName("different").build();
        RenameTagCommand diffOriginal = new RenameTagCommand(differentTag, renamedTag);
        RenameTagCommand diffRenamed = new RenameTagCommand(originalTag, differentTag);
        assertNotEquals(renameTagCommand, diffOriginal);
        assertNotEquals(renameTagCommand, diffRenamed);
    }
}