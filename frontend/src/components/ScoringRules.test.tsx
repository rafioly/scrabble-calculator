import { render, screen } from '@testing-library/react';
import ScoringRules from './ScoringRules';

test('renders scoring rules correctly', () => {
  // Arrange
  const mockRules = {
    A: 1,
    B: 3,
    Z: 10,
  };

  // Act
  render(<ScoringRules scoringRules={mockRules} />);

  // Assert
  expect(screen.getByText(/Scoring Rules/i)).toBeInTheDocument();

  const rowA = screen.getByText('A').closest('p');
  expect(rowA).toHaveTextContent('1');

  const rowB = screen.getByText('B').closest('p');
  expect(rowB).toHaveTextContent('3');

  const rowZ = screen.getByText('Z').closest('p');
  expect(rowZ).toHaveTextContent('10');
});
