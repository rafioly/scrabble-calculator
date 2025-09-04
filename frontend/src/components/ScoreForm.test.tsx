import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import ScoreForm from './ScoreForm';

// Mock props for the ScoreForm component
const mockProps = {
  tiles: Array(10).fill(''),
  score: 0,
  error: null,
  success: null,
  inputRefs: { current: [] },
  handleTileChange: vi.fn(),
  handleKeyDown: vi.fn(),
  handleReset: vi.fn(),
  handleSaveScore: vi.fn(),
  handleViewScores: vi.fn(),
};

test('calls the correct handlers when buttons are clicked', async () => {
  // Arrange
  const user = userEvent.setup();
  render(<ScoreForm {...mockProps} />);

  // Act: Click the "Reset Tiles" button
  const resetButton = screen.getByRole('button', { name: /reset tiles/i });
  await user.click(resetButton);

  // Assert: Check if the reset handler was called
  expect(mockProps.handleReset).toHaveBeenCalledTimes(1);

  // Act: Click the "Save Score" button
  const saveButton = screen.getByRole('button', { name: /save score/i });
  await user.click(saveButton);

  // Assert: Check if the save handler was called
  expect(mockProps.handleSaveScore).toHaveBeenCalledTimes(1);

  // Act: Click the "View Top Scores" button
  const viewScoresButton = screen.getByRole('button', { name: /view top scores/i });
  await user.click(viewScoresButton);

  // Assert: Check if the view scores handler was called
  expect(mockProps.handleViewScores).toHaveBeenCalledTimes(1);
});
